package uimodel;

import dao.BookingDao;
import dao.HouseKeepingServiceDao;
import dao.PaymentDao;
import dao.RoomMasterDao;
import dao.ServiceCategoryDao;
import dao.ServiceDao;
import dao.TableTransactionDao;
import dao.VisitorDao;
import dao.VoucherDao;
import domain.Booking;
import domain.HouseKeepingService;
import domain.Payment;
import domain.Person;
import domain.RoomMaster;
import domain.RoomService;
import domain.ServiceCategory;
import domain.TableMaster;
import domain.TableTransaction;
import domain.Visitor;
import domain.Voucher;
import dto.FrontOfficeCollectionDto;
import enums.EGender;
import enums.EPaymentMode;
import enums.ERoomStatus;
import enums.EType;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.joda.time.DateTime;

/**
 *
 * @author Jean de Dieu HABIMANA @2021
 */
@ManagedBean
@SessionScoped
public class FrontOfficeModel {

    private Person loggedInUser = new Person();
    private List<RoomMaster> allRooms = new RoomMasterDao().findAllSorted();
    private Booking booking = new Booking();
    private Visitor visitor = new Visitor();
    private String gender = new String();
    private String roomMasterId = new String();
    private RoomMaster chosenRoomMaster = new RoomMaster();
    private Booking chosenBooking = new Booking();
    private final Timestamp currentPeriod = new Timestamp(System.currentTimeMillis());
    private String currentDate = new String();
    private String checkInDate = new String();
    private String checkOutDate = new String();
    private long days = 0;
    private String expectedCheckoutHour = new String();
    private String expectedCheckOutMinute = new String();
    private Double roomCharge = 0.0;
    private Double debit = 0.0;
    private Double credit = null;
    private Voucher voucher = new Voucher();
    private String voucherPaymentMode = new String();
    private Double discount = 0.0;
    private Payment chosenPayment = new Payment();
    private List<Voucher> vouchers = new ArrayList<>();
    private EPaymentMode paymentMode = EPaymentMode.CASH;
    private Double voucherTotal = 0.0;
    private String nowDate = new String();
    private String status = new String();
    private Double foodAndBeverageTotal = 0.0;
    private List<ServiceCategory> serviceCategorys = new ServiceCategoryDao().findAll(ServiceCategory.class);
    private List<RoomService> services = new ServiceDao().findAll(RoomService.class);
    private String serviceCategoryId = new String();
    private RoomService chosenRoomService = new RoomService();
    private HouseKeepingService houseKeepingService = new HouseKeepingService();
    private String serviceId = new String();
    private Double roomServiceTotalPrice = 0.0;
    private List<HouseKeepingService> houseKeepingServices = new ArrayList<>();
    private HouseKeepingService chosenHouseKeepingService = new HouseKeepingService();
    private Double totalHouseKeeping = 0.0;
    private List<TableTransaction> tableTransactions = new ArrayList<>();
    private List<Booking> roomBookings = new ArrayList<>();
    private List<Booking> roomPaymentBookings = new ArrayList<>();
    private Date bookingFrom;
    private Date bookingTo;
    private Double amountCash = 0.0;
    private Double amountMomo = 0.0;
    private Double amountCard = 0.0;
    private Double totalAmount = 0.0;
    private String visitorSearchKeyWord = new String();
    private List<Visitor> suggestedVisitors = new ArrayList<>();
    private Person waiter = new Person();
    private Payment payment = new Payment();
    private TableMaster tableMaster = new TableMaster();
    private List<FrontOfficeCollectionDto> roomCollections = new ArrayList<>();
    private Boolean foodAndBeverageRendering = Boolean.FALSE;
    private Boolean extraServiceRendering = Boolean.FALSE;
    private String invoiceRendering = "All";
    private List<Payment> paidRoomTransactions = new ArrayList<>();

    @PostConstruct
    public void init() {
        userInit();
    }

    public void userInit() {
        loggedInUser = (Person) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("session");
    }

    public String checkInRoom() throws ParseException {
        if (booking.getTotalPerson() != (booking.getTotalFemale() + booking.getTotalMale() + booking.getTotalChildren())) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Total of persons not equal"));
            return "booking.xhtml?faces-redirect=true";
        } else {
            if (gender.equalsIgnoreCase("Male")) {
                visitor.setGender(EGender.MALE);
            } else {
                visitor.setGender(EGender.FEMALE);
            }
            new VisitorDao().register(visitor);

            booking.setVisitor(visitor);
            booking.setRoomMaster(chosenRoomMaster);
            booking.setCheckOutPeriod(stringToTimeStamp(new SimpleDateFormat("dd/MM/yyyy").format(booking.getCheckOutDate()) + " " + expectedCheckoutHour + ":" + expectedCheckOutMinute));
            booking.setCheckInPeriod(new Timestamp(System.currentTimeMillis()));

            new BookingDao().register(booking);

            chosenRoomMaster.setRoomStatus(ERoomStatus.TAKEN);
            chosenRoomMaster.setCuurentBookingId(booking.getBookingId());
            chosenRoomMaster.setVisitor(visitor);
            if (booking.getNegociatedPrice() != 0.0) {
                chosenRoomMaster.setIsNegotiated(Boolean.TRUE);
                chosenRoomMaster.setNegotiatedPrice(booking.getNegociatedPrice());
            }
            new RoomMasterDao().update(chosenRoomMaster);
            allRooms = new RoomMasterDao().findAllSorted();

//            booking = new Booking();
            visitor = new Visitor();

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Checked-In"));
            return "add-voucher.xhtml?faces-redirect=true";
        }
    }

    public void updateCheckoutDetails(RoomMaster master) throws ParseException {
        totalHouseKeeping = 0.0;
        voucherTotal = 0.0;
        foodAndBeverageTotal = 0.0;
        chosenRoomMaster = master;
        chosenBooking = new BookingDao().findOne(Booking.class, chosenRoomMaster.getCuurentBookingId());
        tableTransactions = new TableTransactionDao().findByBookingAndPaymentMode(chosenBooking, EPaymentMode.POSTTOROOM);
        checkInDate = new SimpleDateFormat("dd MMM yyyy").format(chosenBooking.getCheckInPeriod());
        currentDate = new SimpleDateFormat("dd MMM yyyy").format(new Date());
        days = dateDifferenceInDays(new SimpleDateFormat("dd MMM yyyy").parse(currentDate), new SimpleDateFormat("dd MMM yyyy").parse(checkInDate));
        houseKeepingServices = new HouseKeepingServiceDao().findByBooking(chosenBooking);

        for (HouseKeepingService s : houseKeepingServices) {
            totalHouseKeeping = totalHouseKeeping + (s.getQuantity() * s.getRoomService().getPrice());
        }

        for (TableTransaction t : tableTransactions) {
            foodAndBeverageTotal = foodAndBeverageTotal + (t.getQuantity() * t.getItem().getUnitRate());
        }

        for (Voucher v : new VoucherDao().findAll(Voucher.class)) {
            if (v.getBooking().getBookingId().equals(chosenBooking.getBookingId())) {
                voucherTotal = voucherTotal + v.getCredit();
            }
        }

        roomCharge = calculateRoomCharge();
    }

    public void searchVisitors() {
        try {
            if (visitorSearchKeyWord.isEmpty() || visitorSearchKeyWord == null) {
                suggestedVisitors = new ArrayList<>();
            } else {
                suggestedVisitors = new VisitorDao().findLikeNameOrDocumentId(visitorSearchKeyWord.toUpperCase());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void chooseVisitor(Visitor it) {
        visitor = it;
        suggestedVisitors = new ArrayList<>();
        visitorSearchKeyWord = visitor.getVisitorName();
    }

    public String redirectCheckoutDetails(RoomMaster master) throws ParseException {
        voucherTotal = 0.0;
        foodAndBeverageTotal = 0.0;
        totalHouseKeeping = 0.0;
        vouchers = new ArrayList<>();
        chosenRoomMaster = master;
        chosenBooking = new BookingDao().findOne(Booking.class, chosenRoomMaster.getCuurentBookingId());
        checkInDate = new SimpleDateFormat("dd MMM yyyy").format(chosenBooking.getCheckInPeriod());
        checkOutDate = new SimpleDateFormat("dd MMM yyyy hh:mm").format(new Date());
        currentDate = new SimpleDateFormat("dd MMM yyyy").format(new Date());
        days = dateDifferenceInDays(new SimpleDateFormat("dd MMM yyyy").parse(currentDate), new SimpleDateFormat("dd MMM yyyy").parse(checkInDate));
        tableTransactions = new TableTransactionDao().findByBookingAndPaymentMode(chosenBooking, EPaymentMode.POSTTOROOM);
        houseKeepingServices = new HouseKeepingServiceDao().findByBooking(chosenBooking);

        chosenPayment = new PaymentDao().findByBookingAndType(chosenBooking, EType.ROOM);
        for (Voucher v : new VoucherDao().findAll(Voucher.class)) {
            if (v.getBooking().getBookingId().equals(chosenBooking.getBookingId())) {
                vouchers.add(v);
                voucherTotal = voucherTotal + v.getCredit();
            }
        }
        roomCharge = calculateRoomCharge();

        for (TableTransaction t : tableTransactions) {
            foodAndBeverageTotal = foodAndBeverageTotal + (t.getQuantity() * t.getItem().getUnitRate());
        }

        for (HouseKeepingService s : houseKeepingServices) {
            totalHouseKeeping = totalHouseKeeping + (s.getQuantity() * s.getRoomService().getPrice());
        }

        for (TableTransaction t : tableTransactions) {
            payment = t.getPayment();
            tableMaster = t.getTableMaster();
            waiter = t.getWaiter();
        }
        return "checkout.xhtml?faces-redirect=true";
    }

    public String redirectReprintInvoice(Booking b) throws ParseException {
        voucherTotal = 0.0;
        foodAndBeverageTotal = 0.0;
        totalHouseKeeping = 0.0;
        vouchers = new ArrayList<>();
        chosenRoomMaster = b.getRoomMaster();
        chosenBooking = b;
        checkInDate = new SimpleDateFormat("dd MMM yyyy").format(chosenBooking.getCheckInPeriod());
        checkOutDate = new SimpleDateFormat("dd MMM yyyy hh:mm").format(chosenBooking.getCheckOutDate());
        currentDate = new SimpleDateFormat("dd MMM yyyy").format(new Date());
        days = dateDifferenceInDays(new SimpleDateFormat("dd MMM yyyy").parse(checkOutDate), new SimpleDateFormat("dd MMM yyyy").parse(checkInDate));
        tableTransactions = new TableTransactionDao().findByBookingAndPaymentMode(chosenBooking, EPaymentMode.POSTTOROOM);
        houseKeepingServices = new HouseKeepingServiceDao().findByBooking(chosenBooking);

        chosenPayment = new PaymentDao().findByBookingAndType(chosenBooking, EType.ROOM);
        for (Voucher v : new VoucherDao().findAll(Voucher.class)) {
            if (v.getBooking().getBookingId().equals(chosenBooking.getBookingId())) {
                vouchers.add(v);
                voucherTotal = voucherTotal + v.getCredit();
            }
        }
        roomCharge = calculateRoomCharge();

        for (TableTransaction t : tableTransactions) {
            foodAndBeverageTotal = foodAndBeverageTotal + (t.getQuantity() * t.getItem().getUnitRate());
        }

        for (HouseKeepingService s : houseKeepingServices) {
            totalHouseKeeping = totalHouseKeeping + (s.getQuantity() * s.getRoomService().getPrice());
        }

        for (TableTransaction t : tableTransactions) {
            payment = t.getPayment();
            tableMaster = t.getTableMaster();
            waiter = t.getWaiter();
        }
        return "invoice.xhtml?faces-redirect=true";
    }

    public String navigateBooking(RoomMaster m) {
        voucher = new Voucher();
        booking = new Booking();
        chosenRoomMaster = m;
        booking.setNegociatedPrice(m.getRoomCategory().getPrice());
        return "booking.xhtml?faces-redirect=true";
    }

    public String navigateService(RoomMaster m) {
        chosenRoomMaster = m;
        chosenBooking = new BookingDao().findOne(Booking.class, chosenRoomMaster.getCuurentBookingId());
        return "guestservice.xhtml?faces-redirect=true";
    }

    public void changeHouseKeepingDetails(RoomMaster r) {
        chosenRoomMaster = r;
    }

    private Double calculateRoomCharge() {
        Double Charge = 0.0;

        DateTime checkInTime = new DateTime(chosenBooking.getCheckInPeriod());
        DateTime checkOutTime = new DateTime(chosenBooking.getCheckOutPeriod());

        switch (Integer.parseInt(days + "")) {
            case 0:
                if (checkInTime.getHourOfDay() >= 0 && checkInTime.getHourOfDay() <= 5) {
                    if (checkOutTime.getHourOfDay() < (11 + chosenBooking.getGraceTime())) {
                        days = 1;
                        Charge = chosenBooking.getRoomMaster().getRoomCategory().getPrice();
                    } else if (checkOutTime.getHourOfDay() < 18) {
                        days = 1;
                        Charge = chosenBooking.getRoomMaster().getRoomCategory().getPrice();
                    } else {
                        days = 2;
                        Charge = chosenBooking.getRoomMaster().getRoomCategory().getPrice() * 2;
                    }
                } else {
                    days = 1;
                    Charge = chosenBooking.getRoomMaster().getRoomCategory().getPrice();
                }
                break;
            default:
                if (checkOutTime.getHourOfDay() < (11 + chosenBooking.getGraceTime())) {
                    Charge = days * chosenBooking.getRoomMaster().getRoomCategory().getPrice();
                } else if (checkOutTime.getHourOfDay() < 18) {
                    Charge = days * chosenBooking.getRoomMaster().getRoomCategory().getPrice();
                } else if (checkOutTime.getHourOfDay() > 18) {
                    days = days + 1;
                    Charge = days * chosenBooking.getRoomMaster().getRoomCategory().getPrice();
                }
                break;
        }

        return Charge;
    }

    public Timestamp stringToTimeStamp(String period) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm");
        Date parsedDate = dateFormat.parse(period);

        return new java.sql.Timestamp(parsedDate.getTime());

    }

    public long dateDifferenceInDays(Date dateFrom, Date dateTo) {
        long diffInMillies = Math.abs(dateTo.getTime() - dateFrom.getTime());
        long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);

        return diff;
    }

    public String registerVoucher() {
        if (credit == null || credit < 0.0) {
            return "add-voucher.xhtml?faces-redirect=true";
        } else {
            voucher.setBooking(booking);
            voucher.setCredit(credit);
            if (voucherPaymentMode.equalsIgnoreCase("CASH")) {
                voucher.setPaymentMode(EPaymentMode.CASH);
            } else if (voucherPaymentMode.equalsIgnoreCase("MoMo")) {
                voucher.setPaymentMode(EPaymentMode.MOBILEMONEY);
            } else if (voucherPaymentMode.equalsIgnoreCase("Card")) {
                voucher.setPaymentMode(EPaymentMode.CARD);
            } else if (voucherPaymentMode.equalsIgnoreCase("Bank")) {
                voucher.setPaymentMode(EPaymentMode.BANK);
            } else {
                voucher.setPaymentMode(EPaymentMode.CASH);
            }
            new VoucherDao().register(voucher);

            Payment payment = new Payment();
            payment.setAmountPaid(voucher.getCredit());
            payment.setPaymentDate(new Date());
            payment.setPaymentMode(voucher.getPaymentMode());
            payment.setPaymentType(EType.ROOM_VOUCHER);
            payment.setStatus("Initialized");
            payment.setCashier(loggedInUser);
            payment.setRoomBooking(booking);
            new PaymentDao().register(payment);
        }

        Payment pay = new Payment();
        pay.setAmountPaid(booking.getRoomMaster().getRoomCategory().getPrice());
        pay.setBillNo(UUID.randomUUID().toString().substring(0, 5));
        pay.setCashier(loggedInUser);
        pay.setStatus("Initialized");
        pay.setRoomBooking(booking);
        pay.setPaymentType(EType.ROOM);
        new PaymentDao().register(pay);

        booking.setPayment(pay);
        new BookingDao().update(booking);

        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Registered"));

        return "print-voucher?faces-redirect=true";
    }

    public String registerAnotherVoucher() {
        voucher.setBooking(chosenBooking);
        voucher.setCredit(credit);
        if (voucherPaymentMode.equalsIgnoreCase("CASH")) {
            voucher.setPaymentMode(EPaymentMode.CASH);
        } else if (voucherPaymentMode.equalsIgnoreCase("MoMo")) {
            voucher.setPaymentMode(EPaymentMode.MOBILEMONEY);
        } else if (voucherPaymentMode.equalsIgnoreCase("Card")) {
            voucher.setPaymentMode(EPaymentMode.CARD);
        } else if (voucherPaymentMode.equalsIgnoreCase("Bank")) {
            voucher.setPaymentMode(EPaymentMode.BANK);
        } else {
            voucher.setPaymentMode(EPaymentMode.CASH);
        }
        new VoucherDao().register(voucher);

        Payment payment = new Payment();
        payment.setAmountPaid(voucher.getCredit());
        payment.setPaymentDate(new Date());
        payment.setPaymentMode(voucher.getPaymentMode());
        payment.setPaymentType(EType.ROOM_VOUCHER);
        payment.setStatus("Initialized");
        payment.setCashier(loggedInUser);
        payment.setRoomBooking(chosenBooking);
        new PaymentDao().register(payment);

        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Registered"));

        return "print-voucher?faces-redirect=true";
    }

    public void updateVoucherAmount() {

    }

    public void findServices() {
        services.clear();
        for (RoomService s : new ServiceDao().findAll(RoomService.class)) {
            if (s.getServiceCategory().getServiceCategoryId().equalsIgnoreCase(serviceCategoryId)) {
                services.add(s);
            }
        }
    }

    public void setChosenService() {
        chosenRoomService = new ServiceDao().findOne(RoomService.class, serviceId);
    }

    public void calculateRoomServiceTotalPrice() {
        if (houseKeepingService.getQuantity() == null || chosenRoomService.getPrice() == null) {
            roomServiceTotalPrice = 0.0;
        } else {
            roomServiceTotalPrice = chosenRoomService.getPrice() * houseKeepingService.getQuantity();
        }
    }

    public void registerDiscount() {
        chosenPayment.setDiscount(discount);
        new PaymentDao().register(chosenPayment);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Registered"));

    }

    public void chooseRoom(RoomMaster r) {
        chosenRoomMaster = r;
        nowDate = new SimpleDateFormat("dd MMM yyyy hh:mm").format(new Date());

    }

    public void chooseService() {
        houseKeepingServices = new HouseKeepingServiceDao().findByBooking(chosenBooking);
    }

    public void navigateEditService(HouseKeepingService service) {
        chosenHouseKeepingService = service;
        roomServiceTotalPrice = chosenHouseKeepingService.getQuantity() * chosenRoomService.getPrice();
    }

    public void availRoomFromCleaning() {
        if (status.equalsIgnoreCase("CLEANED")) {
            chosenRoomMaster.setRoomStatus(ERoomStatus.AVAILABLE);
        } else if (status.equalsIgnoreCase("BLOCKED")) {
            chosenRoomMaster.setRoomStatus(ERoomStatus.REPAIR);
        }
        new RoomMasterDao().update(chosenRoomMaster);
        chosenRoomMaster = new RoomMaster();

        allRooms = new RoomMasterDao().findAllSorted();

        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Updated"));
    }

    public String checkoutRoom() {
        Booking book = new BookingDao().findOne(Booking.class, chosenRoomMaster.getCuurentBookingId());

//        Payment payment = new BookingDao().findOne(Booking.class, chosenRoomMaster.getCuurentBookingId()).getPayment();
        for (Payment payment : new PaymentDao().findByBookingAndStatus(book, "Initialized")) {
            switch (payment.getPaymentType()) {
                case POST_TO_ROOM:
                    if (voucherPaymentMode.equalsIgnoreCase("CASH")) {
                        payment.setPaymentMode(EPaymentMode.CASH);
                        payment.setAmountPaidCash(foodAndBeverageTotal);
                        payment.setAmountPaid(foodAndBeverageTotal);
                    } else if (voucherPaymentMode.equalsIgnoreCase("CREDIT")) {
                        payment.setPaymentMode(EPaymentMode.CREDIT);
                        payment.setAmountPaidCredit(foodAndBeverageTotal);
                        payment.setAmountPaid(foodAndBeverageTotal);
                    } else if (voucherPaymentMode.equalsIgnoreCase("NC")) {
                        payment.setPaymentMode(EPaymentMode.NC);
                        payment.setAmountPaidNC(foodAndBeverageTotal);
                        payment.setAmountPaid(foodAndBeverageTotal);
                    } else if (voucherPaymentMode.equalsIgnoreCase("MOBILEMONEY")) {
                        payment.setPaymentMode(EPaymentMode.MOBILEMONEY);
                        payment.setAmountPaidMomo(foodAndBeverageTotal);
                        payment.setAmountPaid(foodAndBeverageTotal);
                    } else {
                        payment.setPaymentMode(EPaymentMode.CASH);
                        payment.setAmountPaidCash(foodAndBeverageTotal);
                        payment.setAmountPaid(foodAndBeverageTotal);
                    }
                    break;
                case ROOM_VOUCHER:
                    if (voucherPaymentMode.equalsIgnoreCase("CASH")) {
                        payment.setPaymentMode(EPaymentMode.CASH);
                        payment.setAmountPaidCash(voucherTotal);
                        payment.setAmountPaid(voucherTotal);
                    } else if (voucherPaymentMode.equalsIgnoreCase("CREDIT")) {
                        payment.setPaymentMode(EPaymentMode.CREDIT);
                        payment.setAmountPaidCredit(voucherTotal);
                        payment.setAmountPaid(voucherTotal);
                    } else if (voucherPaymentMode.equalsIgnoreCase("NC")) {
                        payment.setPaymentMode(EPaymentMode.NC);
                        payment.setAmountPaidNC(voucherTotal);
                        payment.setAmountPaid(voucherTotal);
                    } else if (voucherPaymentMode.equalsIgnoreCase("MOBILEMONEY")) {
                        payment.setPaymentMode(EPaymentMode.MOBILEMONEY);
                        payment.setAmountPaidMomo(voucherTotal);
                        payment.setAmountPaid(voucherTotal);
                    } else {
                        payment.setPaymentMode(EPaymentMode.CASH);
                        payment.setAmountPaidCash(voucherTotal);
                        payment.setAmountPaid(voucherTotal);
                    }
                    break;
                case ROOM:
                    if (voucherPaymentMode.equalsIgnoreCase("CASH")) {
                        payment.setPaymentMode(EPaymentMode.CASH);
                        payment.setAmountPaidCash(roomCharge - voucherTotal);
                        payment.setAmountPaid(roomCharge - voucherTotal);
                    } else if (voucherPaymentMode.equalsIgnoreCase("CREDIT")) {
                        payment.setPaymentMode(EPaymentMode.CREDIT);
                        payment.setAmountPaidCredit(roomCharge - voucherTotal);
                        payment.setAmountPaid(roomCharge - voucherTotal);
                    } else if (voucherPaymentMode.equalsIgnoreCase("NC")) {
                        payment.setPaymentMode(EPaymentMode.NC);
                        payment.setAmountPaidNC(roomCharge - voucherTotal);
                        payment.setAmountPaid(roomCharge - voucherTotal);
                    } else if (voucherPaymentMode.equalsIgnoreCase("MOBILEMONEY")) {
                        payment.setPaymentMode(EPaymentMode.MOBILEMONEY);
                        payment.setAmountPaidMomo(roomCharge - voucherTotal);
                        payment.setAmountPaid(roomCharge - voucherTotal);
                    } else {
                        payment.setPaymentMode(EPaymentMode.CASH);
                        payment.setAmountPaidCash(roomCharge - voucherTotal);
                        payment.setAmountPaid(roomCharge - voucherTotal);
                    }
                    payment.setDiscount(discount);
                    break;
                case HOUSE_KEEPING:
                    if (voucherPaymentMode.equalsIgnoreCase("CASH")) {
                        payment.setPaymentMode(EPaymentMode.CASH);
                        payment.setAmountPaidCash(totalHouseKeeping);
                        payment.setAmountPaid(totalHouseKeeping);
                    } else if (voucherPaymentMode.equalsIgnoreCase("CREDIT")) {
                        payment.setPaymentMode(EPaymentMode.CREDIT);
                        payment.setAmountPaidCredit(totalHouseKeeping);
                        payment.setAmountPaid(totalHouseKeeping);
                    } else if (voucherPaymentMode.equalsIgnoreCase("NC")) {
                        payment.setPaymentMode(EPaymentMode.NC);
                        payment.setAmountPaidNC(totalHouseKeeping);
                        payment.setAmountPaid(totalHouseKeeping);
                    } else if (voucherPaymentMode.equalsIgnoreCase("MOBILEMONEY")) {
                        payment.setPaymentMode(EPaymentMode.MOBILEMONEY);
                        payment.setAmountPaidMomo(totalHouseKeeping);
                        payment.setAmountPaid(totalHouseKeeping);
                    } else {
                        payment.setPaymentMode(EPaymentMode.CASH);
                        payment.setAmountPaidCash(totalHouseKeeping);
                        payment.setAmountPaid(totalHouseKeeping);
                    }
                    break;
                default:
                    break;
            }
//            payment.setDiscount(discount);
            payment.setPaymentDate(new Date());
            payment.setStatus("Completed");
//            payment.setAmountPaid(foodAndBeverageTotal + totalHouseKeeping + roomCharge - discount);
            new PaymentDao().update(payment);
        }

        chosenRoomMaster.setRoomStatus(ERoomStatus.CLEANING);
        new RoomMasterDao().update(chosenRoomMaster);
        allRooms = new RoomMasterDao().findAllSorted();

        book.setCheckOutDate(new Date());
        book.setCheckOutPeriod(new Timestamp(System.currentTimeMillis()));
        book.setDaysSpent(Integer.parseInt(days + ""));
        new BookingDao().update(book);
        return "main.xhtml?faces-redirect=true";
    }

    public void registerHouseKeepingService() {
        Booking book = new BookingDao().findOne(Booking.class, chosenRoomMaster.getCuurentBookingId());
        houseKeepingService.setRegisterDate(new Date());
        houseKeepingService.setRoomService(chosenRoomService);
        houseKeepingService.setStatus("Initialized");
        houseKeepingService.setBooking(book);
        new HouseKeepingServiceDao().register(houseKeepingService);

        Payment payment = new Payment();
        payment.setAmountPaid(houseKeepingService.getQuantity() * houseKeepingService.getRoomService().getPrice());
        payment.setPaymentDate(new Date());
        payment.setPaymentType(EType.HOUSE_KEEPING);
        payment.setStatus("Initialized");
        payment.setCashier(loggedInUser);
        payment.setRoomBooking(book);
        new PaymentDao().register(payment);

        houseKeepingService = new HouseKeepingService();
        roomServiceTotalPrice = 0.0;

        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Service Added"));
    }

    public void updateHouseKeepingService() {
        chosenHouseKeepingService.setRegisterDate(new Date());
        chosenHouseKeepingService.setRoomService(chosenRoomService);
        chosenHouseKeepingService.setStatus("Initialized");
        chosenHouseKeepingService.setBooking(new BookingDao().findOne(Booking.class, chosenRoomMaster.getCuurentBookingId()));
        new HouseKeepingServiceDao().register(chosenHouseKeepingService);

        chosenHouseKeepingService = new HouseKeepingService();
        roomServiceTotalPrice = 0.0;

        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Service Added"));
    }

    public void findBookingByDate() {
        roomBookings = new BookingDao().findByDateBetween(bookingFrom, bookingTo);
    }

    public void findBookingPaymentByDate() {
        roomCollections = new BookingDao().findByDateBetweenAndPaidGroup(bookingFrom, bookingTo);

        amountCash = 0.0;
        amountCard = 0.0;
        amountMomo = 0.0;
        discount = 0.0;
        totalAmount = 0.0;

        roomPaymentBookings = new BookingDao().findByDateBetweenAndPaid(bookingFrom, bookingTo);

        for (FrontOfficeCollectionDto b : roomCollections) {
            amountCash = amountCash + b.getAmountPaidCash();
            amountMomo = amountMomo + b.getAmountPaidMomo();
            discount = discount + b.getDiscount();
        }
    }

    public void checkRendering() {
        invoiceRendering = new String();

        if (foodAndBeverageRendering && extraServiceRendering) {
            invoiceRendering = "None";
        } else if (extraServiceRendering) {
            invoiceRendering = "ExtraService";
        } else if (foodAndBeverageRendering) {
            invoiceRendering = "FoodAndBeverage";
        } else {
            invoiceRendering = "All";
        }

        System.out.println("Invoice Rendering " + invoiceRendering);
    }

    
    public void retrieveRoomTransactions(Booking p) {
        paidRoomTransactions.clear();
        for (Payment pay : new PaymentDao().findByBookingAndStatus(p, "Completed")) {
            paidRoomTransactions.add(pay);
        }
    }

    public List<RoomMaster> getAllRooms() {
        return allRooms;
    }

    public void setAllRooms(List<RoomMaster> allRooms) {
        this.allRooms = allRooms;
    }

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    public Visitor getVisitor() {
        return visitor;
    }

    public void setVisitor(Visitor visitor) {
        this.visitor = visitor;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getRoomMasterId() {
        return roomMasterId;
    }

    public void setRoomMasterId(String roomMasterId) {
        this.roomMasterId = roomMasterId;
    }

    public RoomMaster getChosenRoomMaster() {
        return chosenRoomMaster;
    }

    public void setChosenRoomMaster(RoomMaster chosenRoomMaster) {
        this.chosenRoomMaster = chosenRoomMaster;
    }

    public Booking getChosenBooking() {
        return chosenBooking;
    }

    public void setChosenBooking(Booking chosenBooking) {
        this.chosenBooking = chosenBooking;
    }

    public Timestamp getCurrentPeriod() {
        return currentPeriod;
    }

    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }

    public String getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(String checkInDate) {
        this.checkInDate = checkInDate;
    }

    public long getDays() {
        return days;
    }

    public void setDays(long days) {
        this.days = days;
    }

    public String getExpectedCheckoutHour() {
        return expectedCheckoutHour;
    }

    public void setExpectedCheckoutHour(String expectedCheckoutHour) {
        this.expectedCheckoutHour = expectedCheckoutHour;
    }

    public String getExpectedCheckOutMinute() {
        return expectedCheckOutMinute;
    }

    public void setExpectedCheckOutMinute(String expectedCheckOutMinute) {
        this.expectedCheckOutMinute = expectedCheckOutMinute;
    }

    public Double getRoomCharge() {
        return roomCharge;
    }

    public void setRoomCharge(Double roomCharge) {
        this.roomCharge = roomCharge;
    }

    public Double getDebit() {
        return debit;
    }

    public void setDebit(Double debit) {
        this.debit = debit;
    }

    public Double getCredit() {
        return credit;
    }

    public void setCredit(Double credit) {
        this.credit = credit;
    }

    public Voucher getVoucher() {
        return voucher;
    }

    public void setVoucher(Voucher voucher) {
        this.voucher = voucher;
    }

    public String getVoucherPaymentMode() {
        return voucherPaymentMode;
    }

    public void setVoucherPaymentMode(String voucherPaymentMode) {
        this.voucherPaymentMode = voucherPaymentMode;
    }

    public Person getLoggedInUser() {
        return loggedInUser;
    }

    public void setLoggedInUser(Person loggedInUser) {
        this.loggedInUser = loggedInUser;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public Payment getChosenPayment() {
        return chosenPayment;
    }

    public void setChosenPayment(Payment chosenPayment) {
        this.chosenPayment = chosenPayment;
    }

    public List<Voucher> getVouchers() {
        return vouchers;
    }

    public void setVouchers(List<Voucher> vouchers) {
        this.vouchers = vouchers;
    }

    public EPaymentMode getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(EPaymentMode paymentMode) {
        this.paymentMode = paymentMode;
    }

    public Double getVoucherTotal() {
        return voucherTotal;
    }

    public void setVoucherTotal(Double voucherTotal) {
        this.voucherTotal = voucherTotal;
    }

    public String getNowDate() {
        return nowDate;
    }

    public void setNowDate(String nowDate) {
        this.nowDate = nowDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getFoodAndBeverageTotal() {
        return foodAndBeverageTotal;
    }

    public void setFoodAndBeverageTotal(Double foodAndBeverageTotal) {
        this.foodAndBeverageTotal = foodAndBeverageTotal;
    }

    public List<ServiceCategory> getServiceCategorys() {
        return serviceCategorys;
    }

    public void setServiceCategorys(List<ServiceCategory> serviceCategorys) {
        this.serviceCategorys = serviceCategorys;
    }

    public List<RoomService> getServices() {
        return services;
    }

    public void setServices(List<RoomService> services) {
        this.services = services;
    }

    public String getServiceCategoryId() {
        return serviceCategoryId;
    }

    public void setServiceCategoryId(String serviceCategoryId) {
        this.serviceCategoryId = serviceCategoryId;
    }

    public RoomService getChosenRoomService() {
        return chosenRoomService;
    }

    public void setChosenRoomService(RoomService chosenRoomService) {
        this.chosenRoomService = chosenRoomService;
    }

    public HouseKeepingService getHouseKeepingService() {
        return houseKeepingService;
    }

    public void setHouseKeepingService(HouseKeepingService houseKeepingService) {
        this.houseKeepingService = houseKeepingService;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public Double getRoomServiceTotalPrice() {
        return roomServiceTotalPrice;
    }

    public void setRoomServiceTotalPrice(Double roomServiceTotalPrice) {
        this.roomServiceTotalPrice = roomServiceTotalPrice;
    }

    public List<HouseKeepingService> getHouseKeepingServices() {
        return houseKeepingServices;
    }

    public void setHouseKeepingServices(List<HouseKeepingService> houseKeepingServices) {
        this.houseKeepingServices = houseKeepingServices;
    }

    public HouseKeepingService getChosenHouseKeepingService() {
        return chosenHouseKeepingService;
    }

    public void setChosenHouseKeepingService(HouseKeepingService chosenHouseKeepingService) {
        this.chosenHouseKeepingService = chosenHouseKeepingService;
    }

    public Double getTotalHouseKeeping() {
        return totalHouseKeeping;
    }

    public void setTotalHouseKeeping(Double totalHouseKeeping) {
        this.totalHouseKeeping = totalHouseKeeping;
    }

    public List<TableTransaction> getTableTransactions() {
        return tableTransactions;
    }

    public void setTableTransactions(List<TableTransaction> tableTransactions) {
        this.tableTransactions = tableTransactions;
    }

    public List<Booking> getRoomBookings() {
        return roomBookings;
    }

    public void setRoomBookings(List<Booking> roomBookings) {
        this.roomBookings = roomBookings;
    }

    public Date getBookingFrom() {
        return bookingFrom;
    }

    public void setBookingFrom(Date bookingFrom) {
        this.bookingFrom = bookingFrom;
    }

    public Date getBookingTo() {
        return bookingTo;
    }

    public void setBookingTo(Date bookingTo) {
        this.bookingTo = bookingTo;
    }

    public List<Booking> getRoomPaymentBookings() {
        return roomPaymentBookings;
    }

    public void setRoomPaymentBookings(List<Booking> roomPaymentBookings) {
        this.roomPaymentBookings = roomPaymentBookings;
    }

    public Double getAmountCash() {
        return amountCash;
    }

    public void setAmountCash(Double amountCash) {
        this.amountCash = amountCash;
    }

    public Double getAmountMomo() {
        return amountMomo;
    }

    public void setAmountMomo(Double amountMomo) {
        this.amountMomo = amountMomo;
    }

    public Double getAmountCard() {
        return amountCard;
    }

    public void setAmountCard(Double amountCard) {
        this.amountCard = amountCard;
    }

    public String getVisitorSearchKeyWord() {
        return visitorSearchKeyWord;
    }

    public void setVisitorSearchKeyWord(String visitorSearchKeyWord) {
        this.visitorSearchKeyWord = visitorSearchKeyWord;
    }

    public List<Visitor> getSuggestedVisitors() {
        return suggestedVisitors;
    }

    public void setSuggestedVisitors(List<Visitor> suggestedVisitors) {
        this.suggestedVisitors = suggestedVisitors;
    }

    public Person getWaiter() {
        return waiter;
    }

    public void setWaiter(Person waiter) {
        this.waiter = waiter;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public TableMaster getTableMaster() {
        return tableMaster;
    }

    public void setTableMaster(TableMaster tableMaster) {
        this.tableMaster = tableMaster;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public List<FrontOfficeCollectionDto> getRoomCollections() {
        return roomCollections;
    }

    public void setRoomCollections(List<FrontOfficeCollectionDto> roomCollections) {
        this.roomCollections = roomCollections;
    }

    public String getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(String checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public Boolean getExtraServiceRendering() {
        return extraServiceRendering;
    }

    public void setExtraServiceRendering(Boolean extraServiceRendering) {
        this.extraServiceRendering = extraServiceRendering;
    }

    public Boolean getFoodAndBeverageRendering() {
        return foodAndBeverageRendering;
    }

    public void setFoodAndBeverageRendering(Boolean foodAndBeverageRendering) {
        this.foodAndBeverageRendering = foodAndBeverageRendering;
    }

    public String getInvoiceRendering() {
        return invoiceRendering;
    }

    public void setInvoiceRendering(String invoiceRendering) {
        this.invoiceRendering = invoiceRendering;
    }

    public List<Payment> getPaidRoomTransactions() {
        return paidRoomTransactions;
    }

    public void setPaidRoomTransactions(List<Payment> paidRoomTransactions) {
        this.paidRoomTransactions = paidRoomTransactions;
    }

}
