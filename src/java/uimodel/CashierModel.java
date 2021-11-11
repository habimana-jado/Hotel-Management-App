package uimodel;

import dao.BookingDao;
import dao.DeletedTransactionDao;
import dao.HotelConfigDao;
import dao.ItemDao;
import dao.PaymentDao;
import dao.PersonDao;
import dao.RoomMasterDao;
import dao.StockDao;
import dao.TableGroupDao;
import dao.TableMasterDao;
import dao.TableTransactionDao;
import dao.UserDepartmentDao;
import domain.Booking;
import domain.DeletedTransaction;
import domain.HotelConfig;
import domain.Item;
import domain.Payment;
import domain.Person;
import domain.RoomMaster;
import domain.Stock;
import domain.TableGroup;
import domain.TableMaster;
import domain.TableTransaction;
import domain.UserDepartment;
import dto.TableTransactionDto;
import enums.EPaymentMode;
import enums.ERoomStatus;
import enums.ETableStatus;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.joda.time.DateTime;

/**
 *
 * @author Jean de Dieu HABIMANA @2020
 */
@ManagedBean
@SessionScoped
public class CashierModel {

    private Person loggedInUser = new Person();
    private HotelConfig hotel = new HotelConfigDao().findOne(HotelConfig.class, Long.parseLong("1"));
    private Item item = new Item();
    private List<Item> items = new ItemDao().findAll(Item.class);
    private TableTransaction tableTransaction = new TableTransaction();
    private String personId = new String();
    private Payment payment = new Payment();
    private List<TableGroup> tableGroups = new TableGroupDao().findAll(TableGroup.class);
    private List<TableMaster> tableMasters = new TableMasterDao().findAllGrouped();
//    private List<TableMaster> roomMasters = new TableMasterDao().findByType("Room");
//    private List<TableMaster> vipRoomMasters = new TableMasterDao().findByType("VipRoom");
    private List<TableTransaction> tableTransactions = new ArrayList<>();
    private List<TableTransaction> foodTableTransactions = new ArrayList<>();
    private List<TableTransaction> beverageTableTransactions = new ArrayList<>();
    private TableMaster chosenTableMaster = new TableMaster();
    private List<TableTransaction> tableTransactions1 = new ArrayList<>();
    private UserDepartment waitery = new UserDepartmentDao().findByDepartment("Waiter");
    private List<Person> waiters = new PersonDao().findByDepartment(waitery);
    private List<Payment> payments = new ArrayList<>();
    private List<Payment> dailySales = new ArrayList<>();
    private String waiterId = new String();
    private List<Item> suggestedItems = new ArrayList<>();
    private String itemSearchKeyWord = new String();
    private Item itemChosen = new Item();
    private TableMasterDao tableDao = new TableMasterDao();
    private String newDate = new SimpleDateFormat("dd MMM yyyy").format(new Date());
    private Double totalBilledBeverage = 0.0;
    private Double totalBilledFoods = 0.0;
    private Double dailyCollection = 0.0;
    private Double dailyBilled = 0.0;
    private Long availableTable;
    private Long billedTable;
    private Long occupiedTable;
    private Date chosenDate = new Date();
    private String payerNumber = new String();
    private String paymentMode = new String();
    private String nowDate = now();
    private String kotType = new String();
    private boolean foodKotType;
    private boolean beverageKotType;
    private List<TableTransaction> paidTableTransactions = new ArrayList<>();
    private Double paymentValue1 = 0.0;
    private Double paymentValue2 = 0.0;
    private Double cash1 = 0.0;
    private Double cash2 = 0.0;
    private Double momo1 = 0.0;
    private Double momo2 = 0.0;
    private Double credit1 = 0.0;
    private Double credit2 = 0.0;
    private Double card1 = 0.0;
    private Double card2 = 0.0;
    private Double momo3 = 0.0;
    private Double cash3 = 0.0;
    private List<TableTransactionDto> tableTransactionDtos = new ArrayList<>();
    private Double discount = 0.0;
    private String roomId = new String();
    private List<RoomMaster> occupiedRooms = new ArrayList<>();

    @PostConstruct
    public void init() {
        userInit();
        tableMasters = new TableMasterDao().findAllGrouped();
        tableGroups = new TableGroupDao().findAll(TableGroup.class);
        waiters = new PersonDao().findByDepartment(waitery);
        dailyCollection = new TableTransactionDao().findTotalByDate(new Date());
        dailyBilled = new TableTransactionDao().findTotalByDateAndTableStatus(new Date(), "Billed");
        availableTable = new TableMasterDao().findTotalByStatus(ETableStatus.VACANT);
        billedTable = new TableMasterDao().findTotalByStatus(ETableStatus.BILLED);
        occupiedTable = new TableMasterDao().findTotalByStatus(ETableStatus.FULL);
    }

    public void retrievePaymentReport() {
        payments = new PaymentDao().findByTransactionDate(chosenDate);

    }

    public void checkPaymentMode() {
        if (paymentMode.equalsIgnoreCase("POSTTOROOM")) {
            occupiedRooms = new RoomMasterDao().findByStatus(ERoomStatus.TAKEN);
        }
    }

    public void retrieveTransactions(Payment p) {
        nowDate = now();
        paidTableTransactions.clear();
        for (TableTransaction tr : p.getTableTransaction()) {
            paidTableTransactions.add(tr);
        }
    }

    public void userInit() {
        nowDate = now();
        loggedInUser = (Person) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("session");
    }

    public void populateTableTransactions(TableMaster tableMaster) {
        nowDate = now();
        chosenTableMaster = tableMaster;
        tableTransactions = new ArrayList<>();
    }

    public String updateTableTransactions() {
        nowDate = now();
        tableTransactions = new TableTransactionDao().findByTableAndStatus(chosenTableMaster, "Sent");
        tableTransactionDtos = new TableTransactionDao().findBillDetails("Sent", chosenTableMaster);
        dailyBilled = new TableTransactionDao().findTotalByDateAndTableStatus(new Date(), "Billed");
        return "print-bill.xhtml?faces-redirect=true";
    }

    public String navigateBillPrint(Payment p) {
        nowDate = now();
        totalBilledFoods = 0.0;
        totalBilledBeverage = 0.0;
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

    public void updateTableBilledTransactions() {
        nowDate = now();
        tableTransactions = new TableTransactionDao().findByTableAndStatus(chosenTableMaster, "Sent");
        dailyBilled = new TableTransactionDao().findTotalByDateAndTableStatus(new Date(), "Billed");
    }

    public String redirectKotPrint() {
        nowDate = now();
        try {

            if (!tableTransactions.isEmpty() || tableTransactions != null) {
                foodTableTransactions.clear();
                beverageTableTransactions.clear();
                foodKotType = Boolean.FALSE;
                beverageKotType = Boolean.FALSE;

                for (TableTransaction t : tableTransactions) {
                    if (t.getItem().getMenuType().equalsIgnoreCase("Food") && t.getPrintStatus().equalsIgnoreCase("UnPrinted")) {
                        kotType = "Food";
                        foodKotType = Boolean.TRUE;
                        foodTableTransactions.add(t);
                    } else if (t.getItem().getMenuType().equalsIgnoreCase("Beverage") && t.getPrintStatus().equalsIgnoreCase("UnPrinted")) {
                        kotType = "Beverage";
                        beverageKotType = Boolean.TRUE;
                        beverageTableTransactions.add(t);
                    } else {
                        kotType = "Both";
                    }
                }
            }

            TableMaster master = new TableMaster();

            for (TableTransaction table : tableTransactions) {
                table.setStatus("Sent");
                table.setPrintStatus("Printed");
                new TableTransactionDao().update(table);

                master = table.getTableMaster();
                master.setTableStatus(ETableStatus.FULL);
                tableDao.update(master);
            }

            tableTransactions = new TableTransactionDao().findByTableAndStatus(chosenTableMaster, "Sent");
            tableMasters = new TableMasterDao().findAllGrouped();

            availableTable = new TableMasterDao().findTotalByStatus(ETableStatus.VACANT);
            billedTable = new TableMasterDao().findTotalByStatus(ETableStatus.BILLED);
            occupiedTable = new TableMasterDao().findTotalByStatus(ETableStatus.FULL);

            return "print-kot.xhtml?faces-redirect=true";

        } catch (Exception e) {
            e.printStackTrace();
            return "main.xhtml?faces-redirect=true";
        }
    }

    public void populateSentTableTransactions(TableMaster tableMaster) {
        nowDate = now();
        chosenTableMaster = tableMaster;
        tableTransactions = new TableTransactionDao().findByTableAndStatus(tableMaster, "Sent");

        totalBilledBeverage = new TableTransactionDao().findTotalByTableAndStatus(tableMaster, "Sent", "Beverage");
        totalBilledFoods = new TableTransactionDao().findTotalByTableAndStatus(tableMaster, "Sent", "Food");

    }

    public static String title[] = new String[]{"Product", "Qty", "Rate", "Amount"};

    public static String kotTitle[] = new String[]{"Product", "Qty"};

    public static String now() {

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss a");
        return sdf.format(cal.getTime());
    }

    public void populateBilledTableTransactions(TableMaster tableMaster) {
        nowDate = now();
        totalBilledBeverage = new TableTransactionDao().findTotalByTableAndStatus(tableMaster, "Sent", "Beverage");
        totalBilledFoods = new TableTransactionDao().findTotalByTableAndStatus(tableMaster, "Sent", "Food");

        tableMaster.setTableStatus(ETableStatus.BILLED);
        new TableMasterDao().update(tableMaster);

        chosenTableMaster = tableMaster;
        tableTransactions = new TableTransactionDao().findByTableAndStatus(tableMaster, "Sent");

        tableMasters = new TableMasterDao().findAllGrouped();
        
        System.out.println("Test Test Test Food = "+totalBilledFoods +" Beverage = "+totalBilledBeverage);
    }

    public void removeUnsavedTransactions() {
        nowDate = now();
        new TableTransactionDao().findByStatus("Pending").forEach((t) -> {
            Item it = t.getItem();
            if (it.getIsFromStock()) {
                it.setQuantity(it.getQuantity() + t.getQuantity());
                new ItemDao().update(it);
            }
            new TableTransactionDao().delete(t);
        });
        tableMasters = new TableMasterDao().findAllGrouped();

        availableTable = new TableMasterDao().findTotalByStatus(ETableStatus.VACANT);
        billedTable = new TableMasterDao().findTotalByStatus(ETableStatus.BILLED);
        occupiedTable = new TableMasterDao().findTotalByStatus(ETableStatus.FULL);
    }

    public void updateBillingTables() {
        nowDate = now();
        tableMasters = new TableMasterDao().findAllGrouped();

        availableTable = new TableMasterDao().findTotalByStatus(ETableStatus.VACANT);
        billedTable = new TableMasterDao().findTotalByStatus(ETableStatus.BILLED);
        occupiedTable = new TableMasterDao().findTotalByStatus(ETableStatus.FULL);
    }

    public void updatePaymentTables() {
        nowDate = now();
        availableTable = new TableMasterDao().findTotalByStatus(ETableStatus.VACANT);
        billedTable = new TableMasterDao().findTotalByStatus(ETableStatus.BILLED);
        occupiedTable = new TableMasterDao().findTotalByStatus(ETableStatus.FULL);
    }

    public void registerTransaction() {
        try {
            createEntry();
        } catch (ParseException ex) {
            Logger.getLogger(CashierModel.class.getName()).log(Level.SEVERE, null, ex);
        }

        nowDate = now();
        Payment p = new Payment();
        p.setPaymentDate(new Date());
        p.setDiscount(0.0);
        try {
            if (itemChosen == null) {
                FacesContext fc = FacesContext.getCurrentInstance();
                fc.addMessage(null, new FacesMessage("Choose Item"));
            } else if (waiterId.isEmpty() || waiterId == null) {
                FacesContext fc = FacesContext.getCurrentInstance();
                fc.addMessage(null, new FacesMessage("Choose Waiter"));
            } else if (itemChosen.getIsFromStock()) {
                if (tableTransaction.getQuantity() > itemChosen.getQuantity()) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Quantity not available, max = " + itemChosen.getQuantity()));
                } else {

                    tableTransactions = new TableTransactionDao().findByTableAndOrStatus(chosenTableMaster, "Pending", "Sent");
                    if (tableTransactions.isEmpty() || tableTransactions == null) {
                        p.setStatus("Inititated");
                        p.setAmountPaid(0.0);
                        p.setPaymentDate(new Date());
                        new PaymentDao().register(p);
                    } else {
                        for (TableTransaction t : tableTransactions) {
                            p = t.getPayment();
                            break;
                        }
                    }
                    tableTransaction.setWaiter(new PersonDao().findOne(Person.class, waiterId));
                    tableTransaction.setPayment(p);
                    tableTransaction.setTransactionDate(new Date());
                    tableTransaction.setStatus("Pending");
                    tableTransaction.setPrintStatus("UnPrinted");
                    tableTransaction.setTableMaster(chosenTableMaster);
                    tableTransaction.setItem(itemChosen);
                    tableTransaction.setTotalPrice(tableTransaction.getQuantity() * itemChosen.getUnitRate());
                    new TableTransactionDao().register(tableTransaction);

                    chosenTableMaster.setPerson(new PersonDao().findOne(Person.class, waiterId));
                    new TableMasterDao().update(chosenTableMaster);

                    if (itemChosen.getIsFromStock()) {
                        itemChosen.setQuantity(itemChosen.getQuantity() - tableTransaction.getQuantity());
                        new ItemDao().update(itemChosen);
                    }

                    tableTransactions = new TableTransactionDao().findByTableAndOrStatus(chosenTableMaster, "Pending", "Sent");

                    tableTransaction = new TableTransaction();

//                    dailyCollection = new TableTransactionDao().findTotalByDate(new Date());
//                    dailyBilled = new TableTransactionDao().findTotalByDateAndTableStatus(new Date(), "Billed");
                    availableTable = new TableMasterDao().findTotalByStatus(ETableStatus.VACANT);
                    billedTable = new TableMasterDao().findTotalByStatus(ETableStatus.BILLED);
                    occupiedTable = new TableMasterDao().findTotalByStatus(ETableStatus.FULL);

                    itemChosen = new Item();
                    itemSearchKeyWord = "";
                }
            } else {

                tableTransactions = new TableTransactionDao().findByTableAndOrStatus(chosenTableMaster, "Pending", "Sent");
                if (tableTransactions.isEmpty() || tableTransactions == null) {
                    p.setStatus("Inititated");
                    p.setAmountPaid(0.0);
                    new PaymentDao().register(p);
                } else {
                    for (TableTransaction t : tableTransactions) {
                        p = t.getPayment();
                        break;
                    }
                }
                tableTransaction.setWaiter(new PersonDao().findOne(Person.class, waiterId));
                tableTransaction.setPayment(p);
                tableTransaction.setTransactionDate(new Date());
                tableTransaction.setStatus("Pending");
                tableTransaction.setPrintStatus("UnPrinted");
                tableTransaction.setTableMaster(chosenTableMaster);
                tableTransaction.setItem(itemChosen);
                tableTransaction.setTotalPrice(tableTransaction.getQuantity() * itemChosen.getUnitRate());
                new TableTransactionDao().register(tableTransaction);

                chosenTableMaster.setPerson(new PersonDao().findOne(Person.class, waiterId));
                new TableMasterDao().update(chosenTableMaster);

                tableTransactions = new TableTransactionDao().findByTableAndOrStatus(chosenTableMaster, "Pending", "Sent");

                tableTransaction = new TableTransaction();

                dailyCollection = new TableTransactionDao().findTotalByDate(new Date());
                dailyBilled = new TableTransactionDao().findTotalByDateAndTableStatus(new Date(), "Billed");

                availableTable = new TableMasterDao().findTotalByStatus(ETableStatus.VACANT);
                billedTable = new TableMasterDao().findTotalByStatus(ETableStatus.BILLED);
                occupiedTable = new TableMasterDao().findTotalByStatus(ETableStatus.FULL);

                itemChosen = new Item();
                itemSearchKeyWord = "";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteTransaction(TableTransaction transaction) {
        nowDate = now();
        try {
            tableTransactions = new TableTransactionDao().findByTableAndOrStatus(chosenTableMaster, "Pending", "Sent");
            if (tableTransactions.size() == 1) {
                TableMaster tm = transaction.getTableMaster();
                tm.setTableStatus(ETableStatus.VACANT);
                new TableMasterDao().update(tm);
            }
            DeletedTransaction deletedTransaction = new DeletedTransaction();
            deletedTransaction.setBillNo(transaction.getBillNo());
            deletedTransaction.setItem(transaction.getItem());
            deletedTransaction.setKotRemarks(transaction.getKotRemarks());
            deletedTransaction.setPayment(transaction.getPayment());
            deletedTransaction.setQuantity(transaction.getQuantity());
            deletedTransaction.setTableMaster(transaction.getTableMaster());
            deletedTransaction.setTotalPrice(transaction.getTotalPrice());
            deletedTransaction.setTransactionDate(transaction.getTransactionDate());

            if (transaction.getTableMaster().getTableStatus().toString().equalsIgnoreCase("FULL")) {
                deletedTransaction.setStatus("Before Billing");
            } else {
                deletedTransaction.setStatus("After Billing");
            }

            new DeletedTransactionDao().register(deletedTransaction);

            new TableTransactionDao().delete(transaction);

            tableTransactions = new TableTransactionDao().findByTableAndOrStatus(chosenTableMaster, "Pending", "Sent");

            tableTransaction = new TableTransaction();

            dailyCollection = new TableTransactionDao().findTotalByDate(new Date());
            dailyBilled = new TableTransactionDao().findTotalByDateAndTableStatus(new Date(), "Billed");

            availableTable = new TableMasterDao().findTotalByStatus(ETableStatus.VACANT);
            billedTable = new TableMasterDao().findTotalByStatus(ETableStatus.BILLED);
            occupiedTable = new TableMasterDao().findTotalByStatus(ETableStatus.FULL);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void createEntry() throws ParseException {

        System.out.println("Test ");
        Date from = new Date();
        Stock lastStock = new Stock();

        List<Stock> list = new StockDao().findAll(Stock.class);

        if (list.isEmpty()) {

            System.out.println("Test 11 ");
            Stock stock1 = new Stock();

            stock1.setStockDate(new Date());

            for (Item i : new ItemDao().findByStockState(true)) {
                stock1.setItem(i);
                stock1.setInitialStock(i.getQuantity());
                stock1.setFinalStock(i.getQuantity());
                stock1.setTotalSale(0.0);
                for (TableTransactionDto t : new TableTransactionDao().findTransactionDetailsByDateBetween("Completed", from, from)) {
                    if (t.getItemName().equalsIgnoreCase(i.getItemName())) {
                        stock1.setTotalSale(t.getQuantity());
                    }
                }
                stock1.setEntryQuantity(0.0);
            }
            new StockDao().register(stock1);

        } else {

            System.out.println("Test 111 ");
            Stock stock = new Stock();
//            System.out.println("Value 1 " + new StockDao().isStockAvailable(from));
//            if (new StockDao().isStockAvailable(from)) {

            System.out.println("Test 1111 ");

            for (Item i : new ItemDao().findByStockState(true)) {

                System.out.println("Test 21 ");

                stock = new StockDao().findByDateAndItem(from, i);

                System.out.println("Test = " + stock);

                if (stock == null) {

                    System.out.println("Reached 1");

                    stock = new Stock();
                    stock.setItem(i);

                    if (new StockDao().findAllByItem(i).size() > 1) {
                        lastStock = new StockDao().findPreviousStockByItem(from, i);

                        System.out.println("Reached 2 ** " + new StockDao().findAllByItem(i).size());
                        if (new StockDao().findAllByItem(i).size() > 2) {
                            System.out.println("Reached 2.x");
                            while (lastStock == null) {
                                from = new DateTime(from).minusDays(1).toDate();
                                lastStock = new StockDao().findPreviousStockByItem(from, i);

                                System.out.println("Reached 3");
                                System.out.println(from);
                            }

                            stock.setInitialStock(lastStock.getFinalStock());
                        }

                    } else {
                        stock.setInitialStock(i.getQuantity());
                    }

                    stock.setStockDate(new Date());

                    stock.setFinalStock(i.getQuantity());
                    stock.setTotalSale(0.0);
                    for (TableTransactionDto t : new TableTransactionDao().findTransactionDetailsByDateBetween("Completed", from, from)) {
                        if (t.getItemName().equalsIgnoreCase(i.getItemName())) {
                            stock.setTotalSale(t.getQuantity());
                        }
                    }

//                        if (new PurchaseDao().findByItemAndDate(from, i) != null) {
//                            for (Purchase p : new PurchaseDao().findByItemAndDate(from, i)) {
//                                stock.setEntryQuantity(stock.getEntryQuantity() + p.getQuantity());
//                            }
//                        } else {
//                            stock.setEntryQuantity(0.0);
//                        }
                    System.out.println("Reached 4");
                    new StockDao().register(stock);

                } else {
                    System.out.println("Test 3 ");
                    stock.setItem(i);

//                    lastStock = new StockDao().findPreviousStockByItem(from, i);
//                    if (new StockDao().findByItem(i) != null) {
//                        lastStock = new StockDao().findPreviousStockByItem(from, i);
//
//                        System.out.println("Reached 2");
//                        while (lastStock == null) {
//                            from = new DateTime(from).minusDays(1).toDate();
//                            lastStock = new StockDao().findPreviousStockByItem(from, i);
//
//                            System.out.println("Reached 3");
//                        }
//
//                        stock.setInitialStock(lastStock.getFinalStock());
//                    }else{
//                        stock.setInitialStock(i.getQuantity());
//                    }
//                    stock.setInitialStock(lastStock.getFinalStock());
                    stock.setFinalStock(i.getQuantity());
//                    stock.setTotalSale(0.0);
                    stock.setStockDate(new Date());

                    for (TableTransactionDto t : new TableTransactionDao().findTransactionDetailsByDateBetween("Completed", from, from)) {
                        if (t.getItemName().equalsIgnoreCase(i.getItemName())) {
                            stock.setTotalSale(t.getQuantity());
                        }
                    }

//                        if (new PurchaseDao().findByItemAndDate(from, i) != null) {
//                            for (Purchase p : new PurchaseDao().findByItemAndDate(from, i)) {
//                                stock.setEntryQuantity(stock.getEntryQuantity() + p.getQuantity());
//                            }
//                        }
                    new StockDao().update(stock);

                }

            }

//            }
        }

    }

    public void completeTransaction() {
        nowDate = now();
        try {
            TableMaster master = new TableMaster();

            for (TableTransaction table : tableTransactions) {
                table.setPrintStatus("Printed");
                table.setStatus("Sent");
                new TableTransactionDao().update(table);

                master = table.getTableMaster();
                master.setTableStatus(ETableStatus.FULL);
                tableDao.update(master);
            }

            tableTransactions = new TableTransactionDao().findByTableAndStatus(chosenTableMaster, "Sent");
            tableMasters = new TableMasterDao().findByType("Table");

            availableTable = new TableMasterDao().findTotalByStatus(ETableStatus.VACANT);
            billedTable = new TableMasterDao().findTotalByStatus(ETableStatus.BILLED);
            occupiedTable = new TableMasterDao().findTotalByStatus(ETableStatus.FULL);

            FacesContext fc = FacesContext.getCurrentInstance();
            fc.addMessage(null, new FacesMessage("Transaction Saved"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String redirectHome() {
        nowDate = now();
        return "main.xhtml?faces-redirect=true";
    }

    public void completeTransactionAndPrint() {
        nowDate = now();
        try {
            TableMaster master = new TableMaster();

            for (TableTransaction table : tableTransactions) {
                table.setStatus("Sent");
                new TableTransactionDao().update(table);

                master = table.getTableMaster();
                master.setTableStatus(ETableStatus.FULL);
                tableDao.update(master);
            }

            tableTransactions = new TableTransactionDao().findByTableAndStatus(chosenTableMaster, "Sent");

            dailyCollection = new TableTransactionDao().findTotalByDate(new Date());
            dailyBilled = new TableTransactionDao().findTotalByDateAndTableStatus(new Date(), "Billed");

            availableTable = new TableMasterDao().findTotalByStatus(ETableStatus.VACANT);
            billedTable = new TableMasterDao().findTotalByStatus(ETableStatus.BILLED);
            occupiedTable = new TableMasterDao().findTotalByStatus(ETableStatus.FULL);

            FacesContext fc = FacesContext.getCurrentInstance();
            fc.addMessage(null, new FacesMessage("Transaction Saved"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void payTransaction() {

        RoomMaster rm = new RoomMasterDao().findOne(RoomMaster.class, roomId);

        nowDate = now();
        try {
            if (paymentMode.equalsIgnoreCase("CASH + MOBILE MONEY")) {
                if (cash1 < 1 || momo1 < 1) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Fill Cash + Momo"));
                }
            } else if (paymentMode.equalsIgnoreCase("CASH + CREDIT")) {
                if (cash2 < 1 || credit1 < 1) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Fill Cash + Credit"));
                }
            } else if (paymentMode.equalsIgnoreCase("CREDIT + MOBILE MONEY")) {
                if (credit2 < 1 || momo2 < 1) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Fill Credit + Momo"));
                }
            } else if (paymentMode.equalsIgnoreCase("CARD + MOBILE MONEY")) {
                if (card1 < 1 || momo3 < 1) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Fill Card + Momo"));
                }
            } else if (paymentMode.equalsIgnoreCase("CASH + CARD")) {
                if (cash3 < 1 || card2 < 1) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Fill Cash + Card"));
                }
            } else {
                TableMaster master = new TableMaster();
                String billId = UUID.randomUUID().toString().substring(0, 5);

                for (TableTransaction table : tableTransactions) {
                    payment = table.getPayment();
                    table.setStatus("Completed");
                    table.setBillNo(billId);
                    new TableTransactionDao().update(table);

                    master = table.getTableMaster();
                    master.setTableStatus(ETableStatus.VACANT);
                    tableDao.update(master);

                    payment.setAmountPaid(payment.getAmountPaid() + table.getTotalPrice());

                }

                if (paymentMode.equalsIgnoreCase("CASH")) {
                    payment.setPaymentMode(EPaymentMode.CASH);
                    payment.setAmountPaidCash(payment.getAmountPaid());
                } else if (paymentMode.equalsIgnoreCase("CREDIT")) {
                    payment.setPaymentMode(EPaymentMode.CREDIT);
                    payment.setAmountPaidCredit(payment.getAmountPaid());
                } else if (paymentMode.equalsIgnoreCase("NC")) {
                    payment.setPaymentMode(EPaymentMode.NC);
                    payment.setAmountPaidNC(payment.getAmountPaid());
                } else if (paymentMode.equalsIgnoreCase("MOBILEMONEY")) {
                    payment.setPaymentMode(EPaymentMode.MOBILEMONEY);
                    payment.setAmountPaidMomo(payment.getAmountPaid());
                } else if (paymentMode.equalsIgnoreCase("CASH + MOBILE MONEY")) {
                    payment.setAmountPaidCash(cash1);
                    payment.setAmountPaidMomo(momo1);
                    payment.setPaymentMode(EPaymentMode.CASH_MOBILEMONEY);
                } else if (paymentMode.equalsIgnoreCase("CASH + CREDIT")) {
                    payment.setAmountPaidCash(cash2);
                    payment.setAmountPaidCredit(credit1);
                    payment.setPaymentMode(EPaymentMode.CASH_CREDIT);
                } else if (paymentMode.equalsIgnoreCase("CREDIT + MOBILE MONEY")) {
                    payment.setAmountPaidCredit(credit2);
                    payment.setAmountPaidMomo(momo2);
                    payment.setPaymentMode(EPaymentMode.MOBILEMONEY_CREDIT);
                } else if (paymentMode.equalsIgnoreCase("CARD + MOBILE MONEY")) {
                    payment.setAmountPaidCard(card1);
                    payment.setAmountPaidMomo(momo3);
                    payment.setPaymentMode(EPaymentMode.MOBILEMONEY_CARD);
                } else if (paymentMode.equalsIgnoreCase("CASH + CARD")) {
                    payment.setAmountPaidCash(cash3);
                    payment.setAmountPaidCard(card2);
                    payment.setPaymentMode(EPaymentMode.CASH_CARD);
                } else if (paymentMode.equalsIgnoreCase("POSTTOROOM")) {
                    payment.setAmountPaidPostToRoom(payment.getAmountPaid());
                    payment.setPaymentMode(EPaymentMode.POSTTOROOM);
                    payment.setRoomBooking(new BookingDao().findOne(Booking.class, rm.getCuurentBookingId()));
                }
                payment.setCashier(loggedInUser);
                payment.setMobileNumber(payerNumber);
                payment.setBillNo(billId);
                payment.setStatus("Completed");
                payment.setPaymentDate(new Date());
                payment.setDiscount(discount);
                new PaymentDao().update(payment);
                payment = new Payment();

                tableTransactions = new TableTransactionDao().findByTableAndStatus(chosenTableMaster, "Completed");

                tableMasters = new TableMasterDao().findAllGrouped();

                availableTable = new TableMasterDao().findTotalByStatus(ETableStatus.VACANT);
                billedTable = new TableMasterDao().findTotalByStatus(ETableStatus.BILLED);
                occupiedTable = new TableMasterDao().findTotalByStatus(ETableStatus.FULL);

                dailyCollection = new TableTransactionDao().findTotalByDate(new Date());
                createEntry();

                FacesContext fc = FacesContext.getCurrentInstance();
                fc.addMessage(null, new FacesMessage("Payment Saved"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void searchItems() {
        nowDate = now();
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

    public void refreshTables() {
        nowDate = now();
        dailyCollection = new TableTransactionDao().findTotalByDate(new Date());
        dailyBilled = new TableTransactionDao().findTotalByDateAndTableStatus(new Date(), "Billed");

        availableTable = new TableMasterDao().findTotalByStatus(ETableStatus.VACANT);
        billedTable = new TableMasterDao().findTotalByStatus(ETableStatus.BILLED);
        occupiedTable = new TableMasterDao().findTotalByStatus(ETableStatus.FULL);

    }

    public void chooseItem(Item it) {
        nowDate = now();
        itemChosen = it;
        suggestedItems = new ArrayList<>();
        itemSearchKeyWord = itemChosen.getItemName();
    }

    public Person getLoggedInUser() {
        return loggedInUser;
    }

    public void setLoggedInUser(Person loggedInUser) {
        this.loggedInUser = loggedInUser;
    }

    public HotelConfig getHotel() {
        return hotel;
    }

    public void setHotel(HotelConfig hotel) {
        this.hotel = hotel;
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

    public TableTransaction getTableTransaction() {
        return tableTransaction;
    }

    public void setTableTransaction(TableTransaction tableTransaction) {
        this.tableTransaction = tableTransaction;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
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

    public List<TableTransaction> getTableTransactions() {
        return tableTransactions;
    }

    public void setTableTransactions(List<TableTransaction> tableTransactions) {
        this.tableTransactions = tableTransactions;
    }

    public TableMaster getChosenTableMaster() {
        return chosenTableMaster;
    }

    public void setChosenTableMaster(TableMaster chosenTableMaster) {
        this.chosenTableMaster = chosenTableMaster;
    }

    public List<TableTransaction> getTableTransactions1() {
        return tableTransactions1;
    }

    public void setTableTransactions1(List<TableTransaction> tableTransactions1) {
        this.tableTransactions1 = tableTransactions1;
    }

    public UserDepartment getWaitery() {
        return waitery;
    }

    public void setWaitery(UserDepartment waitery) {
        this.waitery = waitery;
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

    public TableMasterDao getTableDao() {
        return tableDao;
    }

    public void setTableDao(TableMasterDao tableDao) {
        this.tableDao = tableDao;
    }

    public String getNewDate() {
        return newDate;
    }

    public void setNewDate(String newDate) {
        this.newDate = newDate;
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

    public Double getDailyCollection() {
        return dailyCollection;
    }

    public void setDailyCollection(Double dailyCollection) {
        this.dailyCollection = dailyCollection;
    }

    public Double getDailyBilled() {
        return dailyBilled;
    }

    public void setDailyBilled(Double dailyBilled) {
        this.dailyBilled = dailyBilled;
    }

    public Long getAvailableTable() {
        return availableTable;
    }

    public void setAvailableTable(Long availableTable) {
        this.availableTable = availableTable;
    }

    public Long getBilledTable() {
        return billedTable;
    }

    public void setBilledTable(Long billedTable) {
        this.billedTable = billedTable;
    }

    public Long getOccupiedTable() {
        return occupiedTable;
    }

    public void setOccupiedTable(Long occupiedTable) {
        this.occupiedTable = occupiedTable;
    }

    public Date getChosenDate() {
        return chosenDate;
    }

    public void setChosenDate(Date chosenDate) {
        this.chosenDate = chosenDate;
    }

    public String getPayerNumber() {
        return payerNumber;
    }

    public void setPayerNumber(String payerNumber) {
        this.payerNumber = payerNumber;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public static String[] getTitle() {
        return title;
    }

    public static void setTitle(String[] title) {
        CashierModel.title = title;
    }

    public static String[] getKotTitle() {
        return kotTitle;
    }

    public static void setKotTitle(String[] kotTitle) {
        CashierModel.kotTitle = kotTitle;
    }

    public String getNowDate() {
        return nowDate;
    }

    public void setNowDate(String nowDate) {
        this.nowDate = nowDate;
    }

    public List<TableTransaction> getFoodTableTransactions() {
        return foodTableTransactions;
    }

    public void setFoodTableTransactions(List<TableTransaction> foodTableTransactions) {
        this.foodTableTransactions = foodTableTransactions;
    }

    public List<TableTransaction> getBeverageTableTransactions() {
        return beverageTableTransactions;
    }

    public void setBeverageTableTransactions(List<TableTransaction> beverageTableTransactions) {
        this.beverageTableTransactions = beverageTableTransactions;
    }

    public String getKotType() {
        return kotType;
    }

    public void setKotType(String kotType) {
        this.kotType = kotType;
    }

    public boolean isFoodKotType() {
        return foodKotType;
    }

    public void setFoodKotType(boolean foodKotType) {
        this.foodKotType = foodKotType;
    }

    public boolean isBeverageKotType() {
        return beverageKotType;
    }

    public void setBeverageKotType(boolean beverageKotType) {
        this.beverageKotType = beverageKotType;
    }

    public List<Payment> getPayments() {
        return payments;
    }

    public void setPayments(List<Payment> payments) {
        this.payments = payments;
    }

    public List<Payment> getDailySales() {
        return dailySales;
    }

    public void setDailySales(List<Payment> dailySales) {
        this.dailySales = dailySales;
    }

    public List<TableTransaction> getPaidTableTransactions() {
        return paidTableTransactions;
    }

    public void setPaidTableTransactions(List<TableTransaction> paidTableTransactions) {
        this.paidTableTransactions = paidTableTransactions;
    }

    public Double getPaymentValue1() {
        return paymentValue1;
    }

    public void setPaymentValue1(Double paymentValue1) {
        this.paymentValue1 = paymentValue1;
    }

    public Double getPaymentValue2() {
        return paymentValue2;
    }

    public void setPaymentValue2(Double paymentValue2) {
        this.paymentValue2 = paymentValue2;
    }

    public Double getCash1() {
        return cash1;
    }

    public void setCash1(Double cash1) {
        this.cash1 = cash1;
    }

    public Double getCash2() {
        return cash2;
    }

    public void setCash2(Double cash2) {
        this.cash2 = cash2;
    }

    public Double getMomo1() {
        return momo1;
    }

    public void setMomo1(Double momo1) {
        this.momo1 = momo1;
    }

    public Double getMomo2() {
        return momo2;
    }

    public void setMomo2(Double momo2) {
        this.momo2 = momo2;
    }

    public Double getCredit1() {
        return credit1;
    }

    public void setCredit1(Double credit1) {
        this.credit1 = credit1;
    }

    public Double getCredit2() {
        return credit2;
    }

    public void setCredit2(Double credit2) {
        this.credit2 = credit2;
    }

    public List<TableTransactionDto> getTableTransactionDtos() {
        return tableTransactionDtos;
    }

    public void setTableTransactionDtos(List<TableTransactionDto> tableTransactionDtos) {
        this.tableTransactionDtos = tableTransactionDtos;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public Double getCard1() {
        return card1;
    }

    public void setCard1(Double card1) {
        this.card1 = card1;
    }

    public Double getCard2() {
        return card2;
    }

    public void setCard2(Double card2) {
        this.card2 = card2;
    }

    public Double getMomo3() {
        return momo3;
    }

    public void setMomo3(Double momo3) {
        this.momo3 = momo3;
    }

    public Double getCash3() {
        return cash3;
    }

    public void setCash3(Double cash3) {
        this.cash3 = cash3;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public List<RoomMaster> getOccupiedRooms() {
        return occupiedRooms;
    }

    public void setOccupiedRooms(List<RoomMaster> occupiedRooms) {
        this.occupiedRooms = occupiedRooms;
    }

}
