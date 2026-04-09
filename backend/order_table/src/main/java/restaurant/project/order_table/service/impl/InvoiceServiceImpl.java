package restaurant.project.order_table.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import restaurant.project.order_table.entity.DishEntity;
import restaurant.project.order_table.entity.InvoiceEntity;
import restaurant.project.order_table.entity.InvoiceItemEntity;
import restaurant.project.order_table.entity.TableEntity;
import restaurant.project.order_table.entity.enums.DishStatus;
import restaurant.project.order_table.entity.enums.InvoiceItemStatus;
import restaurant.project.order_table.entity.enums.InvoiceStatus;
import restaurant.project.order_table.entity.enums.TableStatus;
import restaurant.project.order_table.exception.BadRequestException;
import restaurant.project.order_table.repository.DishRepository;
import restaurant.project.order_table.repository.InvoiceItemRepository;
import restaurant.project.order_table.repository.InvoiceRepository;
import restaurant.project.order_table.repository.TableRepository;
import restaurant.project.order_table.service.InvoiceService;
import restaurant.project.order_table.websocket.WebSocketService;

@Service
@RequiredArgsConstructor
public class InvoiceServiceImpl implements InvoiceService {

	private final InvoiceRepository invoiceRepository;
	private final InvoiceItemRepository invoiceItemRepository;
	private final TableRepository tableRepository;
	private final DishRepository dishRepository;
	private final WebSocketService webSocketService;

	@Override
	public InvoiceEntity createInvoice(InvoiceEntity invoice) {
		return invoiceRepository.save(invoice);
	}

	@Override
	public InvoiceEntity getInvoiceById(Long id) {
		return invoiceRepository.findById(id)
				.orElseThrow(() -> new BadRequestException("Invoice not found with id: " + id));
	}

	@Override
	public List<InvoiceEntity> getAllInvoices() {
		return invoiceRepository.findAll();
	}

	@Override
	public InvoiceEntity updateInvoice(Long id, InvoiceEntity invoice) {
		InvoiceEntity existingInvoice = getInvoiceById(id);

		existingInvoice.setTable(invoice.getTable());
		existingInvoice.setStatus(invoice.getStatus());
		existingInvoice.setTotalAmount(invoice.getTotalAmount());

		return invoiceRepository.save(existingInvoice);
	}

	@Override
	public void deleteInvoice(Long id) {
		InvoiceEntity invoice = getInvoiceById(id);
		invoiceRepository.delete(invoice);
	}

	@Override
	public List<InvoiceEntity> getInvoicesByStatus(InvoiceStatus status) {
		return invoiceRepository.findByStatus(status);
	}

	@Override
	public List<InvoiceEntity> getInvoicesByTable(Long tableId) {
		return invoiceRepository.findByTableId(tableId);
	}

	@Override
	public List<InvoiceEntity> getInvoicesByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
		return invoiceRepository.findByCreatedAtBetween(startDate, endDate);
	}

	@Override
	public InvoiceEntity updateInvoiceStatus(Long id, InvoiceStatus status) {
		InvoiceEntity invoice = getInvoiceById(id);
		invoice.setStatus(status);
		InvoiceEntity savedInvoice = invoiceRepository.save(invoice);

		// Gửi thông báo WebSocket về trạng thái hóa đơn/thanh toán
		if (savedInvoice.getTable() != null) {
			webSocketService.sendPaymentNotification(
					savedInvoice.getId(),
					savedInvoice.getTable().getId(),
					status.name());
		}

		return savedInvoice;
	}

	@Override
	public InvoiceEntity getActiveInvoiceByTable(Long tableId) {
		List<InvoiceEntity> invoices = invoiceRepository.findByTableIdAndStatus(tableId, InvoiceStatus.OPEN);
		return invoices.isEmpty() ? null : invoices.get(0);
	}

	@Override
	public InvoiceEntity getActiveInvoiceByTableNumber(Integer tableNumber) {
		// Find table by table number
		TableEntity table = tableRepository.findByTableNumber(tableNumber)
				.orElseThrow(() -> new BadRequestException("Table not found with number: " + tableNumber));

		// Get active invoice for this table
		List<InvoiceEntity> invoices = invoiceRepository.findByTableIdAndStatus(table.getId(), InvoiceStatus.OPEN);
		return invoices.isEmpty() ? null : invoices.get(0);
	}

	@Override
	@Transactional
	public BigDecimal calculateInvoiceTotal(Long id) {
		InvoiceEntity invoice = invoiceRepository.findById(id)
				.orElseThrow(() -> new BadRequestException("Invoice not found with id: " + id));

		// Dùng truy vấn SQL trực tiếp để tính tổng tiền (luôn chính xác và bỏ qua món
		// đã hủy)
		BigDecimal total = invoiceItemRepository.calculateTotalExcludingCancelled(id);

		// Nếu không còn món nào, tổng tiền là 0
		if (total == null) {
			total = BigDecimal.ZERO;
		}

		// Cập nhật và lưu vào database
		invoice.setTotalAmount(total);
		invoiceRepository.saveAndFlush(invoice);

		return total;
	}

	@Override
	@Transactional
	public InvoiceEntity createInvoiceWithItems(Long tableId, List<ItemData> items) {
		// Validate table exists
		TableEntity table = tableRepository.findById(tableId)
				.orElseThrow(() -> new BadRequestException("Table not found with id: " + tableId));

		// Kiểm tra trạng thái bàn và hóa đơn
		// Logic:
		// - Nếu bàn AVAILABLE -> Tạo invoice mới, chuyển bàn sang OCCUPIED
		// - Nếu bàn OCCUPIED + có invoice OPEN -> Thêm món vào invoice hiện tại
		// - Nếu bàn OCCUPIED + không có invoice OPEN -> Lỗi (đã thanh toán nhưng chưa
		// reset bàn)

		InvoiceEntity invoice = null;

		if (table.getStatus() == TableStatus.AVAILABLE) {
			// Bàn trống -> Tạo invoice mới
			invoice = new InvoiceEntity();
			invoice.setTable(table);
			invoice.setStatus(InvoiceStatus.OPEN);
			invoice.setTotalAmount(BigDecimal.ZERO);
			invoice.setItems(new ArrayList<>());

			// Chuyển trạng thái bàn sang OCCUPIED
			table.setStatus(TableStatus.OCCUPIED);
			tableRepository.save(table);

			// Save invoice first to get ID
			invoice = invoiceRepository.save(invoice);

		} else if (table.getStatus() == TableStatus.OCCUPIED) {
			// Bàn đang được sử dụng -> Tìm invoice OPEN
			invoice = getActiveInvoiceByTable(tableId);

			if (invoice == null) {
				// Bàn OCCUPIED nhưng không có invoice OPEN -> Lỗi nghiệp vụ
				throw new BadRequestException(
						"Table is occupied but has no active invoice. Please check table status or contact staff.");
			}
			// Nếu có invoice OPEN -> Thêm món vào invoice hiện tại

		} else {
			// Bàn đang bảo trì hoặc đã đặt -> Không cho phép order
			throw new BadRequestException(
					"Table is not available for ordering. Status: " + table.getStatus());
		}

		// Get existing items for this invoice (để check trùng món)
		List<InvoiceItemEntity> existingItems = invoice.getItems();
		BigDecimal totalAmount = invoice.getTotalAmount();

		// Add new items to invoice
		for (ItemData itemData : items) {
			// Get dish
			DishEntity dish = dishRepository.findById(itemData.dishId)
					.orElseThrow(() -> new BadRequestException("Dish not found with id: " + itemData.dishId));
			if (dish.getStatus() != DishStatus.AVAILABLE) {
				throw new BadRequestException(
						"Món " + dish.getName() + " hiện tại đã hết, vui lòng gọi món khác");
			}
			// Kiểm tra xem món đã có trong hóa đơn chưa (dựa trên dishId), cùng trạng thái
			// (WATTING)
			InvoiceItemEntity existingItem = existingItems.stream()
					.filter(item -> item.getDish().getId().equals(itemData.dishId)
							&& item.getStatus() == itemData.status)
					.findFirst()
					.orElse(null);

			if (existingItem != null) {
				// Món đã có trong hóa đơn -> Cộng dồn số lượng
				int oldQuantity = existingItem.getQuantity();
				int newQuantity = oldQuantity + itemData.quantity;

				existingItem.setQuantity(newQuantity);

				// Recalculate total price for this item
				BigDecimal unitPrice = existingItem.getUnitPrice();
				BigDecimal newItemTotal = unitPrice.multiply(BigDecimal.valueOf(newQuantity));

				// Update total amount (subtract old, add new)
				totalAmount = totalAmount.subtract(existingItem.getTotalPrice());
				existingItem.setTotalPrice(newItemTotal);
				totalAmount = totalAmount.add(newItemTotal);

			} else {
				// Món mới -> Tạo invoice item mới
				InvoiceItemEntity invoiceItem = new InvoiceItemEntity();
				invoiceItem.setInvoice(invoice);
				invoiceItem.setStatus(itemData.status != null ? itemData.status : InvoiceItemStatus.WAITING);
				invoiceItem.setNote(itemData.notes);
				invoiceItem.setDish(dish);
				invoiceItem.setQuantity(itemData.quantity);

				// Convert price from Integer to BigDecimal
				BigDecimal unitPrice = BigDecimal.valueOf(dish.getPrice());
				invoiceItem.setUnitPrice(unitPrice);

				// Calculate total price for this item
				BigDecimal quantityBD = BigDecimal.valueOf(itemData.quantity);
				BigDecimal itemTotal = unitPrice.multiply(quantityBD);
				invoiceItem.setTotalPrice(itemTotal);

				existingItems.add(invoiceItem);
				totalAmount = totalAmount.add(itemTotal);
			}
		}

		// Update invoice total amount
		invoice.setTotalAmount(totalAmount);

		// Save invoice with updated items
		InvoiceEntity savedInvoice = invoiceRepository.save(invoice);

		// Gửi thông báo WebSocket
		webSocketService.sendNewOrderNotification(
				savedInvoice.getId(),
				table.getId(),
				"Đơn hàng mới từ bàn " + (table.getTableNumber() != null ? table.getTableNumber() : table.getId()));

		if (table.getStatus() == TableStatus.OCCUPIED) {
			webSocketService.sendTableStatusUpdate(table.getId(), "OCCUPIED", null);
		}

		return savedInvoice;
	}
}
