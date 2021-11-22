package uimodel;

import common.FileUpload;
import common.PassCode;
import dao.BookingDao;
import dao.DeletedTransactionDao;
import dao.FloorMasterDao;
import dao.HotelConfigDao;
import dao.HouseKeepingServiceDao;
import dao.ItemCategoryDao;
import dao.ItemDao;
import dao.ItemUnitDao;
import dao.PaymentDao;
import dao.PersonDao;
import dao.PurchaseDao;
import dao.RestaurantDao;
import dao.RoomCategoryDao;
import dao.RoomMasterDao;
import dao.ServiceCategoryDao;
import dao.ServiceDao;
import dao.StockDao;
import dao.TableGroupDao;
import dao.TableMasterDao;
import dao.TableTransactionDao;
import dao.UserDepartmentDao;
import dao.VoucherDao;
import domain.Booking;
import domain.DeletedTransaction;
import domain.FloorMaster;
import domain.HotelConfig;
import domain.HouseKeepingService;
import domain.Item;
import domain.ItemCategory;
import domain.ItemUnit;
import domain.Payment;
import domain.Person;
import domain.Purchase;
import domain.Restaurant;
import domain.RoomCategory;
import domain.RoomMaster;
import domain.RoomService;
import domain.ServiceCategory;
import domain.Stock;
import domain.TableGroup;
import domain.TableMaster;
import domain.TableTransaction;
import domain.UserDepartment;
import domain.Voucher;
import dto.FrontOfficeCollectionDto;
import dto.TableTransactionDto;
import enums.EPaymentMode;
import enums.ERoomStatus;
import enums.EStatus;
import enums.ETableStatus;
import enums.EType;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import org.joda.time.DateTime;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.chart.PieChartModel;
import static uimodel.CashierModel.now;

/**
 *
 * @author Jean de Dieu HABIMANA @2020
 */
@ManagedBean
@SessionScoped
public class AdminModel {

    private Person loggedInUser = new Person();
    private Person user = new Person();
    private HotelConfig hotelConfig = new HotelConfig();
    private String userDepartmentId = new String();
    private List<Person> users = new PersonDao().findAll(Person.class);
    private List<UserDepartment> userDepartments = new UserDepartmentDao().findAll(UserDepartment.class);
    private HotelConfig hotel = new HotelConfigDao().findOne(HotelConfig.class, Long.parseLong("1"));
    private final UserDepartment waitery = new UserDepartmentDao().findByDepartment("Waiter");
    private String waiterId = new String();
    private List<Person> waiters = new PersonDao().findByDepartment(waitery);
    private List<String> chosenImage = new ArrayList<>();
    private UserDepartment userDepartment = new UserDepartment();
    private Person editPerson = new Person();
    private List<Restaurant> restaurants = new RestaurantDao().findAll(Restaurant.class);
    private List<TableGroup> tableGroups = new TableGroupDao().findAll(TableGroup.class);
    private List<TableMaster> tableMasters = new TableMasterDao().findAll(TableMaster.class);
    private Restaurant restaurant = new Restaurant();
    private TableGroup tableGroup = new TableGroup();
    private TableMaster tableMaster = new TableMaster();
    private String tableGroupId = new String();
    private String restaurantId = new String();
    private ItemUnit itemUnit = new ItemUnit();
    private List<ItemUnit> itemUnits = new ItemUnitDao().findAll(ItemUnit.class);
    private ItemCategory itemCategory = new ItemCategory();
    private List<ItemCategory> itemCategories = new ItemCategoryDao().findAll(ItemCategory.class);
    private String itemCategoryId = new String();
    private String itemUnitId = new String();
    private Item item = new Item();
    private List<Item> items = new ItemDao().findAll(Item.class);
    private String password = "12345";
    private Double purchasePrice = 0.0;
    private List<Item> suggestedItems = new ArrayList<>();
    private String itemSearchKeyWord = new String();
    private Item itemChosen = new Item();
    private Purchase purchase = new Purchase();
    private List<Purchase> purchases = new PurchaseDao().findExpenses();
    private List<Purchase> itemsPurchased = new PurchaseDao().findItemsPurchasedByDate(new Date());
    private List<TableTransaction> tableTransactions = new TableTransactionDao().findByStatusAndDate("Completed", new Date());
    private Date searchDate = new Date();
    private Date from = new Date();
    private Date to = new Date();
    private Double totalMomo = 0.0;
    private Double totalNc = 0.0;
    private Double totalCash = 0.0;
    private Double totalCredit = 0.0;
    private Double totalCard = 0.0;
    private Double totalExpense = 0.0;
    private Double totalPurchase = 0.0;
    private Double totalDiscount = 0.0;
    private Double totalBilledBeverage = 0.0;
    private Double totalBilledFoods = 0.0;
    private Double totalQuantityIncome = 0.0;
    private String nowDate = now();
    private List<Payment> payments = new ArrayList<>();
    private List<TableTransaction> paidTableTransactions = new ArrayList<>();
    private List<Payment> paidRoomTransactions = new ArrayList<>();
    private TableMaster chosenTableMaster = new TableMaster();
    private String renderingKey = new String();
    private boolean waiterSearch;
    private PieChartModel pieModel = new PieChartModel();
    private List<TableTransactionDto> tableTransactionDtos = new ArrayList<>();
    private List<TableTransactionDto> stockTransactionDtos = new ArrayList<>();
    private Double totalIncome = 0.0;
    private List<DeletedTransaction> deletedTransactions = new DeletedTransactionDao().findAll(DeletedTransaction.class);
    private Item chosenItem = new Item();
    private List<Stock> stocks = new StockDao().findByDate(new Date(), new Date());
    private String dateFrom = new SimpleDateFormat("dd MMM yyyy").format(new Date());
    private String dateTo = new SimpleDateFormat("dd MMM yyyy").format(new Date());
    private List<Payment> creditPayments = new ArrayList<>();
    private Double totalDiscounted = 0.0;
    private Double totalCreditByDate = 0.0;
    private String creditedPersonNumber = new String();
    private List<Payment> creditedPersons = new PaymentDao().findByStatusAndDateBetweenAndMobileNumber("Completed", from, to);
    private String fromHour = "02";
    private String toHour = "01";
    private String fromMinute = "00";
    private String toMinute = "00";
    private String fromDate = new String();
    private String toDate = new String();
    private String billNo = new String();
    private RoomCategory roomCategory = new RoomCategory();
    private RoomCategory chosenRoomCategory = new RoomCategory();
    private FloorMaster floorMaster = new FloorMaster();
    private RoomMaster roomMaster = new RoomMaster();
    private String roomCategoryId = new String();
    private String floorMasterId = new String();
    private List<RoomCategory> roomCategorys = new RoomCategoryDao().findAll(RoomCategory.class);
    private List<FloorMaster> floorMasters = new FloorMasterDao().findAll(FloorMaster.class);
    private List<RoomMaster> roomMasters = new RoomMasterDao().findAll(RoomMaster.class);
    private ServiceCategory serviceCategory = new ServiceCategory();
    private RoomService service = new RoomService();
    private List<ServiceCategory> serviceCategorys = new ServiceCategoryDao().findAll(ServiceCategory.class);
    private List<RoomService> services = new ServiceDao().findAll(RoomService.class);
    private String serviceCategoryId = new String();
    private RoomService chosenRoomService = new RoomService();
    private List<Booking> roomPaymentBookings = new ArrayList<>();
    private Date bookingFrom;
    private Date bookingTo;
    private Double amountCash = 0.0;
    private Double amountMomo = 0.0;
    private Double amountCard = 0.0;
    private Double totalAmount = 0.0;
    private Double discount = 0.0;
    private List<FrontOfficeCollectionDto> roomCollections = new ArrayList<>();
    private Double voucherTotal = 0.0;
    private Double foodAndBeverageTotal = 0.0;
    private List<Voucher> vouchers = new ArrayList<>();
    private Double totalHouseKeeping = 0.0;
    private RoomMaster chosenRoomMaster = new RoomMaster();
    private Booking chosenBooking = new Booking();
    private String currentDate = new String();
    private String checkInDate = new String();
    private String checkOutDate = new String();
    private long days = 0;
    private List<HouseKeepingService> houseKeepingServices = new ArrayList<>();
    private Payment chosenPayment = new Payment();
    private Double roomCharge = 0.0;
    private Person waiter = new Person();
    private Payment payment = new Payment();

    @PostConstruct
    public void init() {
        userInit();
        searchTransactionsInit();
        try {
            searchTableTransactions();
        } catch (ParseException ex) {
            Logger.getLogger(AdminModel.class.getName()).log(Level.SEVERE, null, ex);
        }
        users = new PersonDao().findAll(Person.class);
        restaurants = new RestaurantDao().findAll(Restaurant.class);
        tableGroups = new TableGroupDao().findAll(TableGroup.class);
        tableMasters = new TableMasterDao().findAll(TableMaster.class);
        items = new ItemDao().findAll(Item.class);
        itemCategories = new ItemCategoryDao().findAll(ItemCategory.class);
        itemUnits = new ItemUnitDao().findAll(ItemUnit.class);
        stocks = new StockDao().findByDate(new Date(), new Date());
        creditedPersons = new PaymentDao().findByStatusAndDateBetween("Completed", from, to);
        loadDashboard();
        retrieveCreditPaymentReport();
    }

    public void loadDashboard() {
        Double quant = 0.0;

        Map<String, Double> map = new HashMap<>();

        for (TableTransaction tr : tableTransactions) {
            if (map.containsKey(tr.getItem().getItemName())) {
                quant = map.get(tr.getItem().getItemName());
                map.put(tr.getItem().getItemName(), quant + tr.getQuantity());
            } else {
                map.put(tr.getItem().getItemName(), tr.getQuantity());
            }
        }

        Iterator<Map.Entry<String, Double>> iterator = map.entrySet().iterator();

        // Iterate over the HashMap 
        while (iterator.hasNext()) {
            // Get the entry at this iteration 
            Map.Entry<String, Double> entry = iterator.next();
            pieModel.set(entry.getKey(), entry.getValue());
        }

        pieModel.setLegendPosition("e");
        pieModel.setShowDatatip(true);
        pieModel.setShowDataLabels(true);
        pieModel.setDataFormat("value");
        pieModel.setDataLabelFormatString("%d");
        pieModel.setDiameter(250);
        pieModel.setExtender("customExtender");

    }

    public void userInit() {
        loggedInUser = (Person) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("session");
    }

    public void returnAdmin() throws IOException {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.redirect(ec.getRequestContextPath() + "/pages/Admin/employee.xhtml");
    }

    public void registerServiceCategory() {
        new ServiceCategoryDao().register(serviceCategory);
        serviceCategorys = new ServiceCategoryDao().findAll(ServiceCategory.class);

        serviceCategory = new ServiceCategory();

        FacesContext fc = FacesContext.getCurrentInstance();
        fc.addMessage(null, new FacesMessage("Category Registered"));

    }

    public void registerService() {
        ServiceCategory category = new ServiceCategoryDao().findOne(ServiceCategory.class, serviceCategoryId);
        service.setServiceCategory(category);
        new ServiceDao().register(service);
        service = new RoomService();
        services = new ServiceDao().findAll(RoomService.class);

        FacesContext fc = FacesContext.getCurrentInstance();
        fc.addMessage(null, new FacesMessage("Service Registered"));
    }

    public void updateService() {
        ServiceCategory category = new ServiceCategoryDao().findOne(ServiceCategory.class, serviceCategoryId);
        chosenRoomService.setServiceCategory(category);
        new ServiceDao().update(chosenRoomService);
        chosenRoomService = new RoomService();
        services = new ServiceDao().findAll(RoomService.class);

        FacesContext fc = FacesContext.getCurrentInstance();
        fc.addMessage(null, new FacesMessage("Service Updated"));
    }

    public void updateAccount(Person p) throws Exception {
        UserDepartment dp = new UserDepartmentDao().findOne(UserDepartment.class, userDepartmentId);
        p.setUserDepartment(dp);
        p.setPassword(new PassCode().encrypt(p.getPassword()));
        new PersonDao().update(p);

        FacesContext fc = FacesContext.getCurrentInstance();
        fc.addMessage(null, new FacesMessage("User Updated"));

        users = new PersonDao().findAll(Person.class);
    }

    public void retrieveTransactions(Payment p) {
        paidTableTransactions.clear();
        for (TableTransaction tr : p.getTableTransaction()) {
            paidTableTransactions.add(tr);
        }
    }

    public void retrieveRoomTransactions(Booking p) {
        paidRoomTransactions.clear();
        for (Payment pay : new PaymentDao().findByBookingAndStatus(p, "Completed")) {
            paidRoomTransactions.add(pay);
        }
    }

    public void findServices(ServiceCategory cat) {
        services.clear();
        for (RoomService s : new ServiceCategoryDao().findOne(ServiceCategory.class, cat.getServiceCategoryId()).getRoomServices()) {
            services.add(s);
        }
    }

    public void setChosenService(RoomService r) {
        chosenRoomService = r;
    }

    public void searchDeletedTransactions() {
        deletedTransactions = new DeletedTransactionDao().findByDateBetween(from, to);

        dateFrom = new SimpleDateFormat("dd MMM yyyy").format(from);
        dateTo = new SimpleDateFormat("dd MMM yyyy").format(to);
    }

    public void searchCloseStock() {
        stocks = new StockDao().findByDate(from, to);

        dateFrom = new SimpleDateFormat("dd MMM yyyy").format(from);
        dateTo = new SimpleDateFormat("dd MMM yyyy").format(to);
    }

    public void searchTableTransactions() throws ParseException {
        totalIncome = 0.0;
        totalCredit = 0.0;
        totalMomo = 0.0;
        totalNc = 0.0;
        totalCash = 0.0;
        totalCard = 0.0;
        totalDiscounted = 0.0;

//        tableTransactionDtos = new TableTransactionDao().findTransactionDetailsByDateBetween("Completed", from, to);
        Date fromTime = new SimpleDateFormat("dd/MM/yyyy hh:mm").parse(fromDate + " " + fromHour + ":" + fromMinute);
        Date toTime = new SimpleDateFormat("dd/MM/yyyy hh:mm").parse(toDate + " " + toHour + ":" + toMinute);

        tableTransactionDtos = new TableTransactionDao().findTransactionDetailsByDateAndHourBetweenAndNotRoomPayment("Completed", fromTime, toTime);

        for (Payment p : new PaymentDao().findByStatusAndDateBetween("Completed", fromTime, toTime)) {
            totalDiscounted = totalDiscounted + p.getDiscount();
        }

        for (TableTransactionDto t : tableTransactionDtos) {
            totalIncome = totalIncome + (t.getQuantity() * t.getUnitRate());
        }
        for (Payment t : new PaymentDao().findByStatusAndDateBetweenAndNotRoomPayment("Completed", fromTime, toTime)) {
            switch (t.getPaymentMode()) {
                case CASH:
                    totalCash = totalCash + t.getAmountPaidCash();
                    break;
                case MOBILEMONEY:
                    totalMomo = totalMomo + t.getAmountPaidMomo();
                    break;
                case CREDIT:
                    totalCredit = totalCredit + t.getAmountPaidCredit();
                    break;
                case NC:
                    totalNc = totalNc + t.getAmountPaidNC();
                    break;
                case CARD:
                    totalCard = totalCard + t.getAmountPaidCard();
                    break;
                case CASH_MOBILEMONEY:
                    totalCash = totalCash + t.getAmountPaidCash();
                    totalMomo = totalMomo + t.getAmountPaidMomo();
                    break;
                case MOBILEMONEY_CREDIT:
                    totalMomo = totalMomo + t.getAmountPaidMomo();
                    totalCredit = totalCredit + t.getAmountPaidCredit();
                    break;
                case CASH_CREDIT:
                    totalCash = totalCash + t.getAmountPaidCash();
                    totalCredit = totalCredit + t.getAmountPaidCredit();
                    break;
                case MOBILEMONEY_CARD:
                    totalMomo = totalMomo + t.getAmountPaidMomo();
                    totalCard = totalCard + t.getAmountPaidCard();
                    break;
                case CASH_CARD:
                    totalCash = totalCash + t.getAmountPaidCash();
                    totalCard = totalCard + t.getAmountPaidCard();
                    break;
                default:
                    totalCash = totalCash + t.getAmountPaid();
                    break;
            }
        }
    }

    public String redirectHome() {
        return "daily-sales-report.xhtml?faces-redirect=true";
    }

    public void block(Person p) {
        p.setStatus(EStatus.BLOCKED);
        new PersonDao().update(p);

        FacesContext fc = FacesContext.getCurrentInstance();
        fc.addMessage(null, new FacesMessage("User Blocked"));
        users = new PersonDao().findAll(Person.class);
    }

    public void activate(Person p) {
        p.setStatus(EStatus.ACTIVE);
        new PersonDao().update(p);

        FacesContext fc = FacesContext.getCurrentInstance();
        fc.addMessage(null, new FacesMessage("User Activated"));
        users = new PersonDao().findAll(Person.class);
    }

    public void delete(Person p) {
        new PersonDao().delete(p);

        FacesContext fc = FacesContext.getCurrentInstance();
        fc.addMessage(null, new FacesMessage("User Deleted"));

    }

    public void updateHotel() {
        try {
            new HotelConfigDao().update(hotel);
            hotel = new HotelConfigDao().findOne(HotelConfig.class, 1);

            FacesContext fc = FacesContext.getCurrentInstance();
            fc.addMessage(null, new FacesMessage("Hotel Config Updated"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void itemCategoryRegister() {
        try {
            new ItemCategoryDao().register(itemCategory);

            itemCategory = new ItemCategory();

            FacesContext fc = FacesContext.getCurrentInstance();
            fc.addMessage(null, new FacesMessage("Item Category Registered"));

            itemCategories = new ItemCategoryDao().findAll(ItemCategory.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateItem(Item i) {
        new ItemDao().update(i);
        items = new ItemDao().findAll(Item.class);

        FacesContext fc = FacesContext.getCurrentInstance();
        fc.addMessage(null, new FacesMessage("Item Updated"));
    }

    public void itemUnitRegister() {
        try {
            new ItemUnitDao().register(itemUnit);
            itemUnits = new ItemUnitDao().findAll(ItemUnit.class);
            itemUnit = new ItemUnit();

            FacesContext fc = FacesContext.getCurrentInstance();
            fc.addMessage(null, new FacesMessage("Item Unit Registered"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void Upload(FileUploadEvent event) {
        chosenImage.add(new FileUpload().Upload(event, "C:\\Users\\nizey\\OneDrive\\Documents\\NetBeansProjects\\Market\\HMS\\web\\uploads\\hotel-logo\\"));
    }

    public void registerUserDepartment() {
        try {
            userDepartment.setStatus(EStatus.ACTIVE);
            new UserDepartmentDao().register(userDepartment);

            userDepartment = new UserDepartment();

            userDepartments = new UserDepartmentDao().findAll(UserDepartment.class);

            FacesContext fc = FacesContext.getCurrentInstance();
            fc.addMessage(null, new FacesMessage("Department Registered"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void retrievePaymentReport() {
        payments = new PaymentDao().findByTransactionDate(searchDate);

    }

    public void retrieveCreditPaymentReport() {
        totalCreditByDate = 0.0;
        if (creditedPersonNumber.equalsIgnoreCase("All")) {
            creditPayments = new PaymentDao().findByTransactionDateAndCredit(searchDate);
        } else {
            creditPayments = new PaymentDao().findByTransactionDateAndCreditAndPerson(searchDate, creditedPersonNumber);
        }
        for (Payment p : creditPayments) {
            totalCreditByDate = totalCreditByDate + p.getAmountPaidCredit();
        }
    }

    public void userRegister() {
        try {
            UserDepartment department = new UserDepartmentDao().findOne(UserDepartment.class, userDepartmentId);
            user.setUserDepartment(department);
            user.setStatus(EStatus.ACTIVE);
            user.setUsername(user.getNames());
            user.setPassword(new PassCode().encrypt("1234"));
            new PersonDao().register(user);

            user = new Person();
            users = new PersonDao().findAll(Person.class);

            FacesContext fc = FacesContext.getCurrentInstance();
            fc.addMessage(null, new FacesMessage("User Registered"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void cashierRegister() {
        try {
            UserDepartment department = new UserDepartmentDao().findOne(UserDepartment.class, userDepartmentId);
            user.setUserDepartment(department);
            user.setStatus(EStatus.ACTIVE);
            user.setNames("Morreen");
            new PersonDao().register(user);

            user = new Person();
            users = new PersonDao().findAll(Person.class);

            FacesContext fc = FacesContext.getCurrentInstance();
            fc.addMessage(null, new FacesMessage("User Registered"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void userUpdate() {
        try {
            UserDepartment department = new UserDepartmentDao().findOne(UserDepartment.class, userDepartmentId);
            user.setUserDepartment(department);
//            user.setStatus(EStatus.ACTIVE);
            new PersonDao().update(user);

            users = new PersonDao().findAll(Person.class);

            FacesContext fc = FacesContext.getCurrentInstance();
            fc.addMessage(null, new FacesMessage("User Updated"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void itemRegister() {
        try {
            ItemCategory category = new ItemCategoryDao().findOne(ItemCategory.class, itemCategoryId);
            ItemUnit itemUnit = new ItemUnitDao().findOne(ItemUnit.class, itemUnitId);

            item.setItemCategory(category);
            item.setItemUnit(itemUnit);
            new ItemDao().register(item);

            if (item.getIsFromStock()) {
                Purchase p = new Purchase();
                p.setItem(item);
                p.setQuantity(item.getQuantity());
                p.setPurchasePrice(purchasePrice);
                p.setPurchaseDate(new Date());
                p.setType("Stock");
                new PurchaseDao().register(p);

                Stock st = new Stock();
                st.setEntryQuantity(item.getQuantity());
                st.setFinalStock(item.getQuantity());
                st.setInitialStock(item.getQuantity());
                st.setItem(item);
                st.setStockDate(new Date());
                st.setTotalSale(0.0);
                new StockDao().register(st);
            }

            items = new ItemDao().findAll(Item.class);
            item = new Item();
            searchTransactionsInit();

            FacesContext fc = FacesContext.getCurrentInstance();
            fc.addMessage(null, new FacesMessage("Item Registered"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void itemUpdate() {
        try {
            ItemCategory category = new ItemCategoryDao().findOne(ItemCategory.class, itemCategoryId);
            ItemUnit itemUnit = new ItemUnitDao().findOne(ItemUnit.class, itemUnitId);

            chosenItem.setItemCategory(category);
            chosenItem.setItemUnit(itemUnit);
            new ItemDao().update(chosenItem);

            if (chosenItem.getIsFromStock()) {
                Purchase p = new Purchase();
                p.setItem(chosenItem);
                p.setQuantity(chosenItem.getQuantity());
                p.setPurchasePrice(purchasePrice);
                p.setPurchaseDate(new Date());
                p.setType("Stock");
                new PurchaseDao().register(p);
            }

            items = new ItemDao().findAll(Item.class);
            chosenItem = new Item();

            FacesContext fc = FacesContext.getCurrentInstance();
            fc.addMessage(null, new FacesMessage("Item Updated"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void registerRestaurant() {
        try {
            if (chosenImage.isEmpty()) {
                FacesContext fc = FacesContext.getCurrentInstance();
                fc.addMessage(null, new FacesMessage("Upload Logo"));
            } else {
                for (String x : chosenImage) {
                    restaurant.setLogo(x);
                }
                chosenImage.clear();

                restaurant.setStatus(EStatus.ACTIVE);
                new RestaurantDao().register(restaurant);
                restaurant = new Restaurant();
            }
            restaurants = new RestaurantDao().findAll(Restaurant.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void blockRestaurant(Restaurant rest) {
        if (rest.getStatus().equals(EStatus.ACTIVE)) {
            rest.setStatus(EStatus.BLOCKED);
            new RestaurantDao().update(rest);

            FacesContext fc = FacesContext.getCurrentInstance();
            fc.addMessage(null, new FacesMessage("Restaurant Blocked"));
        } else {
            rest.setStatus(EStatus.ACTIVE);
            new RestaurantDao().update(rest);

            FacesContext fc = FacesContext.getCurrentInstance();
            fc.addMessage(null, new FacesMessage("Restaurant Activated"));
        }
    }

    public void registerTableGroup() {
        new TableGroupDao().register(tableGroup);
        tableGroup = new TableGroup();
        tableGroups = new TableGroupDao().findAll(TableGroup.class);

        FacesContext fc = FacesContext.getCurrentInstance();
        fc.addMessage(null, new FacesMessage("Table Group Registered"));
    }

    public void registerTableMaster() {
        Restaurant restaurant = new RestaurantDao().findOne(Restaurant.class, restaurantId);
        TableGroup group = new TableGroupDao().findOne(TableGroup.class, tableGroupId);

        System.out.println("Restaurant: " + restaurantId);
        System.out.println("Table: " + tableGroupId);

        tableMaster.setTableGroup(group);
        tableMaster.setTableStatus(ETableStatus.VACANT);
        tableMaster.setRestaurant(restaurant);

        new TableMasterDao().register(tableMaster);
        tableMaster = new TableMaster();

        tableMasters = new TableMasterDao().findAll(TableMaster.class);
        FacesContext fc = FacesContext.getCurrentInstance();
        fc.addMessage(null, new FacesMessage("Table Master Registered"));
    }

    public void searchItems() {
        try {
            if (itemSearchKeyWord.isEmpty() || itemSearchKeyWord == null) {
                suggestedItems = new ArrayList<>();
            } else {
                suggestedItems = new ItemDao().findLikeNameAndQuantity(itemSearchKeyWord.toUpperCase(), 0.0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void chooseItem(Item it) {
        itemChosen = it;
        suggestedItems = new ArrayList<>();
        itemSearchKeyWord = itemChosen.getItemName();
    }

    public void purchaseRegister() {
        try {
            ItemUnit itemUnit = new ItemUnitDao().findOne(ItemUnit.class, itemUnitId);

            purchase.setItemUnit(itemUnit);
            purchase.setType("Expense");
            new PurchaseDao().register(purchase);
            purchases = new PurchaseDao().findExpenses();
            purchase = new Purchase();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Expense Registered"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String navigatePayCredit(Payment p) {
        return "test.xhtml?faces-redirect=true";
    }

    public String navigateBillPrint(Payment p) {
        nowDate = now();
        totalBilledFoods = 0.0;
        totalBilledBeverage = 0.0;
        billNo = p.getBillNo();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss a");

        tableTransactions.clear();
        for (TableTransaction tr : p.getTableTransaction()) {
            tableTransactions.add(tr);
            chosenTableMaster = tr.getTableMaster();
            if (tr.getItem().getMenuType().equalsIgnoreCase("Food")) {
                totalBilledFoods = totalBilledFoods + tr.getTotalPrice();
            } else if (tr.getItem().getMenuType().equalsIgnoreCase("Beverage")) {
                totalBilledBeverage = totalBilledBeverage + tr.getTotalPrice();
            }
            nowDate = sdf.format(tr.getTransactionDate());
        }
        return "print-bill.xhtml?faces-redirect=true";
    }

    public String navigateEditEmployee(Person person) {
        editPerson = person;
        return "employee.xhtml?faces-redirect=true";
    }

    public void searchTransactions() {
        totalMomo = 0.0;
        totalCash = 0.0;
        totalCredit = 0.0;
        totalCard = 0.0;
        totalNc = 0.0;
        totalExpense = 0.0;
        totalPurchase = 0.0;

        if (waiterId.equalsIgnoreCase("all")) {
            tableTransactions = new TableTransactionDao().findByStatusAndDate("Completed", searchDate);
        } else {
            Person p = new PersonDao().findOne(Person.class, waiterId);
            tableTransactions = new TableTransactionDao().findByStatusAndDateAndPerson("Completed", searchDate, p);
        }
        purchases = new PurchaseDao().findItemsPurchasedByDateAndType(searchDate, "Expense");
        itemsPurchased = new PurchaseDao().findItemsPurchasedByDateAndType(searchDate, "Stock");

        for (TableTransaction t : tableTransactions) {
            switch (t.getPayment().getPaymentMode()) {
                case CASH:
                    totalCash = totalCash + t.getPayment().getAmountPaidCash();
                    break;
                case MOBILEMONEY:
                    totalMomo = totalMomo + t.getPayment().getAmountPaidMomo();
                    break;
                case CREDIT:
                    totalCredit = totalCredit + t.getPayment().getAmountPaidCredit();
                    break;
                case NC:
                    totalNc = totalNc + t.getPayment().getAmountPaidNC();
                    break;
                case CARD:
                    totalCard = totalCard + t.getPayment().getAmountPaidCard();
                    break;
                case CASH_MOBILEMONEY:
                    totalCash = totalCash + t.getPayment().getAmountPaidCash();
                    totalMomo = totalMomo + t.getPayment().getAmountPaidMomo();
                    break;
                case MOBILEMONEY_CREDIT:
                    totalMomo = totalMomo + t.getPayment().getAmountPaidMomo();
                    totalCredit = totalCredit + t.getPayment().getAmountPaidCredit();
                    break;
                case CASH_CREDIT:
                    totalCash = totalCash + t.getPayment().getAmountPaidCash();
                    totalCredit = totalCredit + t.getPayment().getAmountPaidCredit();
                    break;
                case MOBILEMONEY_CARD:
                    totalMomo = totalMomo + t.getPayment().getAmountPaidMomo();
                    totalCard = totalCard + t.getPayment().getAmountPaidCard();
                    break;
                case CASH_CARD:
                    totalCash = totalCash + t.getPayment().getAmountPaidCash();
                    totalCard = totalCard + t.getPayment().getAmountPaidCard();
                    break;
                default:
                    totalCash = totalCash + t.getTotalPrice();
                    break;
            }
        }

        for (Purchase p : purchases) {
            totalExpense = totalExpense + (p.getPurchasePrice());
        }
        for (Purchase p : itemsPurchased) {
            totalPurchase = totalPurchase + (p.getPurchasePrice() * p.getQuantity());
        }
    }

    public void searchTransactionsByDateBetween() {
        totalMomo = 0.0;
        totalCash = 0.0;
        totalCredit = 0.0;
        totalCard = 0.0;
        totalNc = 0.0;
        totalQuantityIncome = 0.0;

        if (itemUnitId.equalsIgnoreCase("all")) {
            tableTransactions = new TableTransactionDao().findByStatusAndDateBetween("Completed", from, to);
        } else {
            Item it = new ItemDao().findOne(Item.class, itemUnitId);
            tableTransactions = new TableTransactionDao().findByStatusAndDatesBetweenAndItem("Completed", from, to, it);
        }

        for (TableTransaction t : tableTransactions) {
            switch (t.getPayment().getPaymentMode()) {
                case CASH:
                    totalCash = totalCash + t.getPayment().getAmountPaidCash();
                    break;
                case MOBILEMONEY:
                    totalMomo = totalMomo + t.getPayment().getAmountPaidMomo();
                    break;
                case CREDIT:
                    totalCredit = totalCredit + t.getPayment().getAmountPaidCredit();
                    break;
                case NC:
                    totalNc = totalNc + t.getPayment().getAmountPaidNC();
                    break;
                case CARD:
                    totalCard = totalCard + t.getPayment().getAmountPaidCard();
                    break;
                case CASH_MOBILEMONEY:
                    totalCash = totalCash + t.getPayment().getAmountPaidCash();
                    totalMomo = totalMomo + t.getPayment().getAmountPaidMomo();
                    break;
                case MOBILEMONEY_CREDIT:
                    totalMomo = totalMomo + t.getPayment().getAmountPaidMomo();
                    totalCredit = totalCredit + t.getPayment().getAmountPaidCredit();
                    break;
                case CASH_CREDIT:
                    totalCash = totalCash + t.getPayment().getAmountPaidCash();
                    totalCredit = totalCredit + t.getPayment().getAmountPaidCredit();
                    break;
                case MOBILEMONEY_CARD:
                    totalMomo = totalMomo + t.getPayment().getAmountPaidMomo();
                    totalCard = totalCard + t.getPayment().getAmountPaidCard();
                    break;
                case CASH_CARD:
                    totalCash = totalCash + t.getPayment().getAmountPaidCash();
                    totalCard = totalCard + t.getPayment().getAmountPaidCard();
                    break;
                default:
                    totalCash = totalCash + t.getTotalPrice();
                    break;
            }
            totalQuantityIncome = totalQuantityIncome + t.getQuantity();
        }
    }

    public void searchExpensesByDateBetween() {
        totalExpense = 0.0;
        totalPurchase = 0.0;

        purchases = new PurchaseDao().findItemsPurchasedByDatesBetweenAndType(from, to, "Expense");
        itemsPurchased = new PurchaseDao().findItemsPurchasedByDatesBetweenAndType(from, to, "Stock");

        for (Purchase p : purchases) {
            totalExpense = totalExpense + (p.getPurchasePrice());
        }
        for (Purchase p : itemsPurchased) {
            totalPurchase = totalPurchase + (p.getPurchasePrice() * p.getQuantity());
        }
    }

    public void searchTransactionsByItem() {
        totalMomo = 0.0;
        totalCash = 0.0;
        totalCard = 0.0;
        totalCredit = 0.0;
        totalExpense = 0.0;
        totalPurchase = 0.0;
        totalNc = 0.0;

        if (itemUnitId.equalsIgnoreCase("all")) {
            tableTransactions = new TableTransactionDao().findByStatus("Completed");
        } else {
            Person p = new PersonDao().findOne(Person.class, waiterId);
            tableTransactions = new TableTransactionDao().findByStatusAndDatesBetweenAndPerson("Completed", from, to, p);
        }
        purchases = new PurchaseDao().findItemsPurchasedByDateAndType(searchDate, "Expense");
        itemsPurchased = new PurchaseDao().findItemsPurchasedByDateAndType(searchDate, "Stock");

        for (TableTransaction t : tableTransactions) {
            switch (t.getPayment().getPaymentMode()) {
                case CASH:
                    totalCash = totalCash + t.getPayment().getAmountPaidCash();
                    break;
                case MOBILEMONEY:
                    totalMomo = totalMomo + t.getPayment().getAmountPaidMomo();
                    break;
                case CREDIT:
                    totalCredit = totalCredit + t.getPayment().getAmountPaidCredit();
                    break;
                case NC:
                    totalNc = totalNc + t.getPayment().getAmountPaidNC();
                    break;
                case CARD:
                    totalCard = totalCard + t.getPayment().getAmountPaidCard();
                    break;
                case CASH_MOBILEMONEY:
                    totalCash = totalCash + t.getPayment().getAmountPaidCash();
                    totalMomo = totalMomo + t.getPayment().getAmountPaidMomo();
                    break;
                case MOBILEMONEY_CREDIT:
                    totalMomo = totalMomo + t.getPayment().getAmountPaidMomo();
                    totalCredit = totalCredit + t.getPayment().getAmountPaidCredit();
                    break;
                case CASH_CREDIT:
                    totalCash = totalCash + t.getPayment().getAmountPaidCash();
                    totalCredit = totalCredit + t.getPayment().getAmountPaidCredit();
                    break;
                case MOBILEMONEY_CARD:
                    totalMomo = totalMomo + t.getPayment().getAmountPaidMomo();
                    totalCard = totalCard + t.getPayment().getAmountPaidCard();
                    break;
                case CASH_CARD:
                    totalCash = totalCash + t.getPayment().getAmountPaidCash();
                    totalCard = totalCard + t.getPayment().getAmountPaidCard();
                    break;
                default:
                    totalCash = totalCash + t.getTotalPrice();
                    break;
            }

            for (Purchase p : purchases) {
                totalExpense = totalExpense + (p.getPurchasePrice());
            }
            for (Purchase p : itemsPurchased) {
                totalPurchase = totalPurchase + (p.getPurchasePrice() * p.getQuantity());
            }
            tableTransactionDtos = new TableTransactionDao().findTransactionDetailsByDateBetween("Completed", from, to);
            for (TableTransactionDto tt : tableTransactionDtos) {
                totalIncome = totalIncome + (tt.getQuantity() * tt.getUnitRate());
            }
        }

        for (Purchase p : purchases) {
            totalExpense = totalExpense + (p.getPurchasePrice());
        }
        for (Purchase p : itemsPurchased) {
            totalPurchase = totalPurchase + (p.getPurchasePrice() * p.getQuantity());
        }

        loadDashboard();
    }

    public void searchTransactionsInit() {
        totalMomo = 0.0;
        totalCash = 0.0;
        totalCredit = 0.0;
        totalExpense = 0.0;
        totalPurchase = 0.0;
        totalCard = 0.0;
        totalNc = 0.0;
        creditedPersonNumber = "All";

        tableTransactions = new TableTransactionDao().findByStatus("Completed");
        purchases = new PurchaseDao().findItemsPurchasedByType("Expense");
        itemsPurchased = new PurchaseDao().findItemsPurchasedByType("Stock");

        for (TableTransaction t : tableTransactions) {
            if (t.getPayment().getStatus().equalsIgnoreCase("Completed")) {
                switch (t.getPayment().getPaymentMode()) {
                    case CASH:
                        totalCash = totalCash + t.getPayment().getAmountPaidCash();
                        break;
                    case MOBILEMONEY:
                        totalMomo = totalMomo + t.getPayment().getAmountPaidMomo();
                        break;
                    case CREDIT:
                        totalCredit = totalCredit + t.getPayment().getAmountPaidCredit();
                        break;
                    case NC:
                        totalNc = totalNc + t.getPayment().getAmountPaidNC();
                        break;
                    case CARD:
                        totalCard = totalCard + t.getPayment().getAmountPaidCard();
                        break;
                    case CASH_MOBILEMONEY:
                        totalCash = totalCash + t.getPayment().getAmountPaidCash();
                        totalMomo = totalMomo + t.getPayment().getAmountPaidMomo();
                        break;
                    case MOBILEMONEY_CREDIT:
                        totalMomo = totalMomo + t.getPayment().getAmountPaidMomo();
                        totalCredit = totalCredit + t.getPayment().getAmountPaidCredit();
                        break;
                    case CASH_CREDIT:
                        totalCash = totalCash + t.getPayment().getAmountPaidCash();
                        totalCredit = totalCredit + t.getPayment().getAmountPaidCredit();
                        break;
                    case MOBILEMONEY_CARD:
                        totalMomo = totalMomo + t.getPayment().getAmountPaidMomo();
                        totalCard = totalCard + t.getPayment().getAmountPaidCard();
                        break;
                    case CASH_CARD:
                        totalCash = totalCash + t.getPayment().getAmountPaidCash();
                        totalCard = totalCard + t.getPayment().getAmountPaidCard();
                        break;
                    default:
                        totalCash = totalCash + t.getTotalPrice();
                        break;
                }
            }
            for (Purchase p : purchases) {
                totalExpense = totalExpense + (p.getPurchasePrice());
            }
            for (Purchase p : itemsPurchased) {
                totalPurchase = totalPurchase + (p.getPurchasePrice() * p.getQuantity());
            }
            tableTransactionDtos = new TableTransactionDao().findTransactionDetailsByDateBetween("Completed", from, to);
            for (TableTransactionDto tt : tableTransactionDtos) {
                totalIncome = totalIncome + (tt.getQuantity() * tt.getUnitRate());
            }
        }

    }

    public void renderSearch() {
        if (renderingKey.equalsIgnoreCase("Waiter")) {
            waiterSearch = true;
        } else {
            waiterSearch = false;
        }
    }

    public void registerRoomCategory() {
        roomCategory.setStatus(EStatus.ACTIVE);
        new RoomCategoryDao().register(roomCategory);
        roomCategorys = new RoomCategoryDao().findAll(RoomCategory.class);
        roomCategory = new RoomCategory();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Room Category Registered"));
    }

    public void updateRoomCategory() {
        new RoomCategoryDao().update(chosenRoomCategory);
        roomCategorys = new RoomCategoryDao().findAll(RoomCategory.class);
        roomCategory = new RoomCategory();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Room Category Updated"));
    }

    public void registerFloorMaster() {
        floorMaster.setStatus(EStatus.ACTIVE);
        new FloorMasterDao().register(floorMaster);
        floorMasters = new FloorMasterDao().findAll(FloorMaster.class);
        floorMaster = new FloorMaster();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Floor Master Registered"));
    }

    public void registerRoom() {
        roomMaster.setRoomStatus(ERoomStatus.AVAILABLE);
        roomMaster.setFloorMaster(new FloorMasterDao().findOne(FloorMaster.class, floorMasterId));
        roomMaster.setRoomCategory(new RoomCategoryDao().findOne(RoomCategory.class, roomCategoryId));
        new RoomMasterDao().register(roomMaster);
        roomMasters = new RoomMasterDao().findAll(RoomMaster.class);
        roomMaster = new RoomMaster();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Room Master Registered"));
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

    private long dateDifferenceInDays(Date dateFrom, Date dateTo) {
        long diffInMillies = Math.abs(dateTo.getTime() - dateFrom.getTime());
        long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);

        return diff;
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
    
    public void chooseItemEdit(Item i) {
        chosenItem = i;
    }

    public void chooseRoomCategoryEdit(RoomCategory r) {
        chosenRoomCategory = r;
    }

    public void chooseServiceCategoryEdit(RoomService r) {
        chosenRoomService = r;
    }

    public Person getLoggedInUser() {
        return loggedInUser;
    }

    public void setLoggedInUser(Person loggedInUser) {
        this.loggedInUser = loggedInUser;
    }

    public Person getUser() {
        return user;
    }

    public void setUser(Person user) {
        this.user = user;
    }

    public HotelConfig getHotelConfig() {
        return hotelConfig;
    }

    public void setHotelConfig(HotelConfig hotelConfig) {
        this.hotelConfig = hotelConfig;
    }

    public String getUserDepartmentId() {
        return userDepartmentId;
    }

    public void setUserDepartmentId(String userDepartmentId) {
        this.userDepartmentId = userDepartmentId;
    }

    public List<Person> getUsers() {
        return users;
    }

    public void setUsers(List<Person> users) {
        this.users = users;
    }

    public List<UserDepartment> getUserDepartments() {
        return userDepartments;
    }

    public void setUserDepartments(List<UserDepartment> userDepartments) {
        this.userDepartments = userDepartments;
    }

    public HotelConfig getHotel() {
        return hotel;
    }

    public void setHotel(HotelConfig hotel) {
        this.hotel = hotel;
    }

    public List<String> getChosenImage() {
        return chosenImage;
    }

    public void setChosenImage(List<String> chosenImage) {
        this.chosenImage = chosenImage;
    }

    public UserDepartment getUserDepartment() {
        return userDepartment;
    }

    public void setUserDepartment(UserDepartment userDepartment) {
        this.userDepartment = userDepartment;
    }

    public Person getEditPerson() {
        return editPerson;
    }

    public void setEditPerson(Person editPerson) {
        this.editPerson = editPerson;
    }

    public List<Restaurant> getRestaurants() {
        return restaurants;
    }

    public void setRestaurants(List<Restaurant> restaurants) {
        this.restaurants = restaurants;
    }

    public List<TableGroup> getTableGroups() {
        return tableGroups;
    }

    public void setTableGroups(List<TableGroup> tableGroups) {
        this.tableGroups = tableGroups;
    }

    public List<TableMaster> getTableMasters() {
        return tableMasters;
    }

    public void setTableMasters(List<TableMaster> tableMasters) {
        this.tableMasters = tableMasters;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public TableGroup getTableGroup() {
        return tableGroup;
    }

    public void setTableGroup(TableGroup tableGroup) {
        this.tableGroup = tableGroup;
    }

    public TableMaster getTableMaster() {
        return tableMaster;
    }

    public void setTableMaster(TableMaster tableMaster) {
        this.tableMaster = tableMaster;
    }

    public String getTableGroupId() {
        return tableGroupId;
    }

    public void setTableGroupId(String tableGroupId) {
        this.tableGroupId = tableGroupId;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    public ItemUnit getItemUnit() {
        return itemUnit;
    }

    public void setItemUnit(ItemUnit itemUnit) {
        this.itemUnit = itemUnit;
    }

    public List<ItemUnit> getItemUnits() {
        return itemUnits;
    }

    public void setItemUnits(List<ItemUnit> itemUnits) {
        this.itemUnits = itemUnits;
    }

    public ItemCategory getItemCategory() {
        return itemCategory;
    }

    public void setItemCategory(ItemCategory itemCategory) {
        this.itemCategory = itemCategory;
    }

    public List<ItemCategory> getItemCategories() {
        return itemCategories;
    }

    public void setItemCategories(List<ItemCategory> itemCategories) {
        this.itemCategories = itemCategories;
    }

    public String getItemCategoryId() {
        return itemCategoryId;
    }

    public void setItemCategoryId(String itemCategoryId) {
        this.itemCategoryId = itemCategoryId;
    }

    public String getItemUnitId() {
        return itemUnitId;
    }

    public void setItemUnitId(String itemUnitId) {
        this.itemUnitId = itemUnitId;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Double getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(Double purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public List<Item> getSuggestedItems() {
        return suggestedItems;
    }

    public void setSuggestedItems(List<Item> suggestedItems) {
        this.suggestedItems = suggestedItems;
    }

    public String getItemSearchKeyWord() {
        return itemSearchKeyWord;
    }

    public void setItemSearchKeyWord(String itemSearchKeyWord) {
        this.itemSearchKeyWord = itemSearchKeyWord;
    }

    public Item getItemChosen() {
        return itemChosen;
    }

    public void setItemChosen(Item itemChosen) {
        this.itemChosen = itemChosen;
    }

    public Purchase getPurchase() {
        return purchase;
    }

    public void setPurchase(Purchase purchase) {
        this.purchase = purchase;
    }

    public List<Purchase> getPurchases() {
        return purchases;
    }

    public void setPurchases(List<Purchase> purchases) {
        this.purchases = purchases;
    }

    public List<TableTransaction> getTableTransactions() {
        return tableTransactions;
    }

    public void setTableTransactions(List<TableTransaction> tableTransactions) {
        this.tableTransactions = tableTransactions;
    }

    public Date getSearchDate() {
        return searchDate;
    }

    public void setSearchDate(Date searchDate) {
        this.searchDate = searchDate;
    }

    public List<Purchase> getItemsPurchased() {
        return itemsPurchased;
    }

    public void setItemsPurchased(List<Purchase> itemsPurchased) {
        this.itemsPurchased = itemsPurchased;
    }

    public Double getTotalMomo() {
        return totalMomo;
    }

    public void setTotalMomo(Double totalMomo) {
        this.totalMomo = totalMomo;
    }

    public Double getTotalCash() {
        return totalCash;
    }

    public void setTotalCash(Double totalCash) {
        this.totalCash = totalCash;
    }

    public Double getTotalCredit() {
        return totalCredit;
    }

    public void setTotalCredit(Double totalCredit) {
        this.totalCredit = totalCredit;
    }

    public Double getTotalExpense() {
        return totalExpense;
    }

    public void setTotalExpense(Double totalExpense) {
        this.totalExpense = totalExpense;
    }

    public Double getTotalPurchase() {
        return totalPurchase;
    }

    public void setTotalPurchase(Double totalPurchase) {
        this.totalPurchase = totalPurchase;
    }

    public Double getTotalBilledBeverage() {
        return totalBilledBeverage;
    }

    public void setTotalBilledBeverage(Double totalBilledBeverage) {
        this.totalBilledBeverage = totalBilledBeverage;
    }

    public Double getTotalBilledFoods() {
        return totalBilledFoods;
    }

    public void setTotalBilledFoods(Double totalBilledFoods) {
        this.totalBilledFoods = totalBilledFoods;
    }

    public String getNowDate() {
        return nowDate;
    }

    public void setNowDate(String nowDate) {
        this.nowDate = nowDate;
    }

    public List<Payment> getPayments() {
        return payments;
    }

    public void setPayments(List<Payment> payments) {
        this.payments = payments;
    }

    public List<TableTransaction> getPaidTableTransactions() {
        return paidTableTransactions;
    }

    public void setPaidTableTransactions(List<TableTransaction> paidTableTransactions) {
        this.paidTableTransactions = paidTableTransactions;
    }

    public TableMaster getChosenTableMaster() {
        return chosenTableMaster;
    }

    public void setChosenTableMaster(TableMaster chosenTableMaster) {
        this.chosenTableMaster = chosenTableMaster;
    }

    public List<Person> getWaiters() {
        return waiters;
    }

    public void setWaiters(List<Person> waiters) {
        this.waiters = waiters;
    }

    public String getWaiterId() {
        return waiterId;
    }

    public void setWaiterId(String waiterId) {
        this.waiterId = waiterId;
    }

    public Date getFrom() {
        return from;
    }

    public void setFrom(Date from) {
        this.from = from;
    }

    public Date getTo() {
        return to;
    }

    public void setTo(Date to) {
        this.to = to;
    }

    public String getRenderingKey() {
        return renderingKey;
    }

    public void setRenderingKey(String renderingKey) {
        this.renderingKey = renderingKey;
    }

    public boolean isWaiterSearch() {
        return waiterSearch;
    }

    public void setWaiterSearch(boolean waiterSearch) {
        this.waiterSearch = waiterSearch;
    }

    public Double getTotalQuantityIncome() {
        return totalQuantityIncome;
    }

    public void setTotalQuantityIncome(Double totalQuantityIncome) {
        this.totalQuantityIncome = totalQuantityIncome;
    }

    public PieChartModel getPieModel() {
        return pieModel;
    }

    public void setPieModel(PieChartModel pieModel) {
        this.pieModel = pieModel;
    }

    public List<TableTransactionDto> getTableTransactionDtos() {
        return tableTransactionDtos;
    }

    public void setTableTransactionDtos(List<TableTransactionDto> tableTransactionDtos) {
        this.tableTransactionDtos = tableTransactionDtos;
    }

    public Double getTotalIncome() {
        return totalIncome;
    }

    public void setTotalIncome(Double totalIncome) {
        this.totalIncome = totalIncome;
    }

    public Double getTotalNc() {
        return totalNc;
    }

    public void setTotalNc(Double totalNc) {
        this.totalNc = totalNc;
    }

    public List<DeletedTransaction> getDeletedTransactions() {
        return deletedTransactions;
    }

    public void setDeletedTransactions(List<DeletedTransaction> deletedTransactions) {
        this.deletedTransactions = deletedTransactions;
    }

    public Item getChosenItem() {
        return chosenItem;
    }

    public void setChosenItem(Item chosenItem) {
        this.chosenItem = chosenItem;
    }

    public List<TableTransactionDto> getStockTransactionDtos() {
        return stockTransactionDtos;
    }

    public void setStockTransactionDtos(List<TableTransactionDto> stockTransactionDtos) {
        this.stockTransactionDtos = stockTransactionDtos;
    }

    public List<Stock> getStocks() {
        return stocks;
    }

    public void setStocks(List<Stock> stocks) {
        this.stocks = stocks;
    }

    public String getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(String dateFrom) {
        this.dateFrom = dateFrom;
    }

    public String getDateTo() {
        return dateTo;
    }

    public void setDateTo(String dateTo) {
        this.dateTo = dateTo;
    }

    public List<Payment> getCreditPayments() {
        return creditPayments;
    }

    public void setCreditPayments(List<Payment> creditPayments) {
        this.creditPayments = creditPayments;
    }

    public Double getTotalDiscount() {
        return totalDiscount;
    }

    public void setTotalDiscount(Double totalDiscount) {
        this.totalDiscount = totalDiscount;
    }

    public Double getTotalDiscounted() {
        return totalDiscounted;
    }

    public void setTotalDiscounted(Double totalDiscounted) {
        this.totalDiscounted = totalDiscounted;
    }

    public Double getTotalCard() {
        return totalCard;
    }

    public void setTotalCard(Double totalCard) {
        this.totalCard = totalCard;
    }

    public Double getTotalCreditByDate() {
        return totalCreditByDate;
    }

    public void setTotalCreditByDate(Double totalCreditByDate) {
        this.totalCreditByDate = totalCreditByDate;
    }

    public String getCreditedPersonNumber() {
        return creditedPersonNumber;
    }

    public void setCreditedPersonNumber(String creditedPersonNumber) {
        this.creditedPersonNumber = creditedPersonNumber;
    }

    public List<Payment> getCreditedPersons() {
        return creditedPersons;
    }

    public void setCreditedPersons(List<Payment> creditedPersons) {
        this.creditedPersons = creditedPersons;
    }

    public String getFromHour() {
        return fromHour;
    }

    public void setFromHour(String fromHour) {
        this.fromHour = fromHour;
    }

    public String getToHour() {
        return toHour;
    }

    public void setToHour(String toHour) {
        this.toHour = toHour;
    }

    public String getFromMinute() {
        return fromMinute;
    }

    public void setFromMinute(String fromMinute) {
        this.fromMinute = fromMinute;
    }

    public String getToMinute() {
        return toMinute;
    }

    public void setToMinute(String toMinute) {
        this.toMinute = toMinute;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public RoomCategory getRoomCategory() {
        return roomCategory;
    }

    public void setRoomCategory(RoomCategory roomCategory) {
        this.roomCategory = roomCategory;
    }

    public FloorMaster getFloorMaster() {
        return floorMaster;
    }

    public void setFloorMaster(FloorMaster floorMaster) {
        this.floorMaster = floorMaster;
    }

    public RoomMaster getRoomMaster() {
        return roomMaster;
    }

    public void setRoomMaster(RoomMaster roomMaster) {
        this.roomMaster = roomMaster;
    }

    public String getRoomCategoryId() {
        return roomCategoryId;
    }

    public void setRoomCategoryId(String roomCategoryId) {
        this.roomCategoryId = roomCategoryId;
    }

    public String getFloorMasterId() {
        return floorMasterId;
    }

    public void setFloorMasterId(String floorMasterId) {
        this.floorMasterId = floorMasterId;
    }

    public List<RoomCategory> getRoomCategorys() {
        return roomCategorys;
    }

    public void setRoomCategorys(List<RoomCategory> roomCategorys) {
        this.roomCategorys = roomCategorys;
    }

    public List<FloorMaster> getFloorMasters() {
        return floorMasters;
    }

    public void setFloorMasters(List<FloorMaster> floorMasters) {
        this.floorMasters = floorMasters;
    }

    public List<RoomMaster> getRoomMasters() {
        return roomMasters;
    }

    public void setRoomMasters(List<RoomMaster> roomMasters) {
        this.roomMasters = roomMasters;
    }

    public ServiceCategory getServiceCategory() {
        return serviceCategory;
    }

    public void setServiceCategory(ServiceCategory serviceCategory) {
        this.serviceCategory = serviceCategory;
    }

    public RoomService getService() {
        return service;
    }

    public void setService(RoomService service) {
        this.service = service;
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

    public List<Payment> getPaidRoomTransactions() {
        return paidRoomTransactions;
    }

    public void setPaidRoomTransactions(List<Payment> paidRoomTransactions) {
        this.paidRoomTransactions = paidRoomTransactions;
    }

    public RoomCategory getChosenRoomCategory() {
        return chosenRoomCategory;
    }

    public void setChosenRoomCategory(RoomCategory chosenRoomCategory) {
        this.chosenRoomCategory = chosenRoomCategory;
    }

    public List<Booking> getRoomPaymentBookings() {
        return roomPaymentBookings;
    }

    public void setRoomPaymentBookings(List<Booking> roomPaymentBookings) {
        this.roomPaymentBookings = roomPaymentBookings;
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

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public List<FrontOfficeCollectionDto> getRoomCollections() {
        return roomCollections;
    }

    public void setRoomCollections(List<FrontOfficeCollectionDto> roomCollections) {
        this.roomCollections = roomCollections;
    }

    public Double getVoucherTotal() {
        return voucherTotal;
    }

    public void setVoucherTotal(Double voucherTotal) {
        this.voucherTotal = voucherTotal;
    }

    public Double getFoodAndBeverageTotal() {
        return foodAndBeverageTotal;
    }

    public void setFoodAndBeverageTotal(Double foodAndBeverageTotal) {
        this.foodAndBeverageTotal = foodAndBeverageTotal;
    }

    public List<Voucher> getVouchers() {
        return vouchers;
    }

    public void setVouchers(List<Voucher> vouchers) {
        this.vouchers = vouchers;
    }

    public Double getTotalHouseKeeping() {
        return totalHouseKeeping;
    }

    public void setTotalHouseKeeping(Double totalHouseKeeping) {
        this.totalHouseKeeping = totalHouseKeeping;
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

    public String getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(String checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public long getDays() {
        return days;
    }

    public void setDays(long days) {
        this.days = days;
    }

    public List<HouseKeepingService> getHouseKeepingServices() {
        return houseKeepingServices;
    }

    public void setHouseKeepingServices(List<HouseKeepingService> houseKeepingServices) {
        this.houseKeepingServices = houseKeepingServices;
    }

    public Payment getChosenPayment() {
        return chosenPayment;
    }

    public void setChosenPayment(Payment chosenPayment) {
        this.chosenPayment = chosenPayment;
    }

    public Double getRoomCharge() {
        return roomCharge;
    }

    public void setRoomCharge(Double roomCharge) {
        this.roomCharge = roomCharge;
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

}
