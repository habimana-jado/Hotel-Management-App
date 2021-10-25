package uimodel;

import dao.HotelConfigDao;
import dao.ItemCategoryDao;
import dao.ItemDao;
import dao.ItemUnitDao;
import dao.PaymentDao;
import dao.PersonDao;
import dao.PurchaseDao;
import dao.StockDao;
import dao.TableGroupDao;
import dao.TableMasterDao;
import dao.TableTransactionDao;
import dao.UserDepartmentDao;
import domain.HotelConfig;
import domain.Item;
import domain.ItemCategory;
import domain.ItemUnit;
import domain.Payment;
import domain.Person;
import domain.Purchase;
import domain.Stock;
import domain.TableGroup;
import domain.TableMaster;
import domain.TableTransaction;
import domain.UserDepartment;
import enums.EPaymentMode;
import enums.ETableStatus;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 *
 * @author Jean de Dieu HABIMANA @2020
 */
@ManagedBean
@SessionScoped
public class RestaurantModel {

    private Person loggedInUser = new Person();
    private HotelConfig hotel = new HotelConfigDao().findOne(HotelConfig.class, Long.parseLong("1"));
    private Item item = new Item();
    private List<Item> items = new ItemDao().findAll(Item.class);
    private TableTransaction tableTransaction = new TableTransaction();
    private String personId = new String();
    private Payment payment = new Payment();
    private List<TableGroup> tableGroups = new TableGroupDao().findAll(TableGroup.class);
    private List<TableMaster> tableMasters = new TableMasterDao().findByType("Table");
    private List<TableMaster> roomMasters = new TableMasterDao().findByType("Room");
    private List<TableMaster> vipRoomMasters = new TableMasterDao().findByType("VipRoom");
    private List<TableTransaction> tableTransactions = new ArrayList<>();
    private List<TableTransaction> foodTableTransactions = new ArrayList<>();
    private List<TableTransaction> beverageTableTransactions = new ArrayList<>();
    private TableMaster chosenTableMaster = new TableMaster();
    private List<TableTransaction> tableTransactions1 = new ArrayList<>();
    private UserDepartment waitery = new UserDepartmentDao().findByDepartment("Waiter");
    private List<Person> waiters = new PersonDao().findByDepartment(waitery);
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
    private Double quantity;
    private Double unitPrice;
    private Double totalPrice;
    private Double purchasePrice;

    @PostConstruct
    public void init() {
        userInit();
        tableMasters = new TableMasterDao().findByType("Table");
        roomMasters = new TableMasterDao().findByType("Room");
        vipRoomMasters = new TableMasterDao().findByType("VipRoom");
        tableGroups = new TableGroupDao().findAll(TableGroup.class);
        waiters = new PersonDao().findByDepartment(waitery);
        dailyCollection = new TableTransactionDao().findTotalByDate(new Date());
        dailyBilled = new TableTransactionDao().findTotalByDateAndTableStatus(new Date(), "Billed");
        availableTable = new TableMasterDao().findTotalByStatus(ETableStatus.VACANT);
        billedTable = new TableMasterDao().findTotalByStatus(ETableStatus.BILLED);
        occupiedTable = new TableMasterDao().findTotalByStatus(ETableStatus.FULL);
            
    }

    public void userInit() {
        loggedInUser = (Person) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("session");
    }

    public void populateTableTransactions(TableMaster tableMaster) {
        chosenTableMaster = tableMaster;
        tableTransactions = new ArrayList<>();
    }

    public void populateSentTableTransactions(TableMaster tableMaster) {
        chosenTableMaster = tableMaster;
        tableTransactions = new TableTransactionDao().findByTableAndStatus(tableMaster, "Sent");

        totalBilledBeverage = new TableTransactionDao().findTotalByTableAndStatus(tableMaster, "Sent", "Beverage");
        totalBilledFoods = new TableTransactionDao().findTotalByTableAndStatus(tableMaster, "Sent", "Food");

        tableMaster.setTableStatus(ETableStatus.BILLED);
        new TableMasterDao().update(tableMaster);

        tableMasters = new TableMasterDao().findByType("Table");

        roomMasters = new TableMasterDao().findByType("Room");
        vipRoomMasters = new TableMasterDao().findByType("VipRoom");

    }

    public String redirectHome() {
        return "main.xhtml?faces-redirect=true";
    }

    public static String title[] = new String[]{"Product", "Qty", "Rate", "Amount"};

    public static String kotTitle[] = new String[]{"Product", "Qty"};

    public static String now() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss a");
        return sdf.format(cal.getTime());
    }

    public void populateBilledTableTransactions(TableMaster tableMaster) {

        totalBilledBeverage = new TableTransactionDao().findTotalByTableAndStatus(tableMaster, "Sent", "Beverage");
        totalBilledFoods = new TableTransactionDao().findTotalByTableAndStatus(tableMaster, "Sent", "Food");

        tableMaster.setTableStatus(ETableStatus.BILLED);
        new TableMasterDao().update(tableMaster);

        chosenTableMaster = tableMaster;
        tableTransactions = new TableTransactionDao().findByTableAndStatus(tableMaster, "Sent");

        tableMasters = new TableMasterDao().findByType("Table");

        roomMasters = new TableMasterDao().findByType("Room");
        vipRoomMasters = new TableMasterDao().findByType("VipRoom");
    }

    public void removeUnsavedTransactions() {
        new TableTransactionDao().findByStatus("Pending").forEach((t) -> {
            new TableTransactionDao().delete(t);
        });
        tableMasters = new TableMasterDao().findByType("Table");

        roomMasters = new TableMasterDao().findByType("Room");
        vipRoomMasters = new TableMasterDao().findByType("VipRoom");

        availableTable = new TableMasterDao().findTotalByStatus(ETableStatus.VACANT);
        billedTable = new TableMasterDao().findTotalByStatus(ETableStatus.BILLED);
        occupiedTable = new TableMasterDao().findTotalByStatus(ETableStatus.FULL);
    }

    public void updateBillingTables() {
        tableMasters = new TableMasterDao().findByType("Table");

        roomMasters = new TableMasterDao().findByType("Room");
        vipRoomMasters = new TableMasterDao().findByType("VipRoom");

        availableTable = new TableMasterDao().findTotalByStatus(ETableStatus.VACANT);
        billedTable = new TableMasterDao().findTotalByStatus(ETableStatus.BILLED);
        occupiedTable = new TableMasterDao().findTotalByStatus(ETableStatus.FULL);

        tableTransactions = new TableTransactionDao().findByTableAndStatus(chosenTableMaster, "Sent");
        for (TableTransaction t : tableTransactions) {
            System.out.println(t.getItem().getItemName());
        }
    }

    public void updatePaymentTables() {

        availableTable = new TableMasterDao().findTotalByStatus(ETableStatus.VACANT);
        billedTable = new TableMasterDao().findTotalByStatus(ETableStatus.BILLED);
        occupiedTable = new TableMasterDao().findTotalByStatus(ETableStatus.FULL);
    }

    public void registerTransaction() {
        try {
            if (itemChosen == null) {
                FacesContext fc = FacesContext.getCurrentInstance();
                fc.addMessage(null, new FacesMessage("Choose Item"));
            } else if (waiterId.isEmpty() || waiterId == null) {
                FacesContext fc = FacesContext.getCurrentInstance();
                fc.addMessage(null, new FacesMessage("Choose Waiter"));
            } else {
                tableTransaction.setPrintStatus("UnPrinted");
                tableTransaction.setTransactionDate(new Date());
                tableTransaction.setStatus("Pending");
//            tableTransaction.setPerson(new PersonDao().findOne(Person.class, waiterId));
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
        try {
            tableTransactions = new TableTransactionDao().findByTableAndOrStatus(chosenTableMaster, "Pending", "Sent");
            if (tableTransactions.size() == 1) {
                TableMaster tm = transaction.getTableMaster();
                tm.setTableStatus(ETableStatus.VACANT);
                new TableMasterDao().update(tm);
            }
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

    public String updateTableTransactions() {
        tableTransactions = new TableTransactionDao().findByTableAndStatus(chosenTableMaster, "Sent");
        return "print-bill.xhtml?faces-redirect=true";
    }

    public String updateBilledTableTransactions() {
        tableTransactions = new TableTransactionDao().findByTableAndStatus(chosenTableMaster, "Sent");
        return "print-bill.xhtml?faces-redirect=true";
    }

    public void completeTransaction() {
        System.out.println("Here Here Here");
        try {
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
            tableMasters = new TableMasterDao().findByType("Table");

            roomMasters = new TableMasterDao().findByType("Room");
            vipRoomMasters = new TableMasterDao().findByType("VipRoom");

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

    public String redirectKotPrint() {
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
            tableMasters = new TableMasterDao().findByType("Table");

            roomMasters = new TableMasterDao().findByType("Room");
            vipRoomMasters = new TableMasterDao().findByType("VipRoom");

            dailyCollection = new TableTransactionDao().findTotalByDate(new Date());
            dailyBilled = new TableTransactionDao().findTotalByDateAndTableStatus(new Date(), "Billed");

            availableTable = new TableMasterDao().findTotalByStatus(ETableStatus.VACANT);
            billedTable = new TableMasterDao().findTotalByStatus(ETableStatus.BILLED);
            occupiedTable = new TableMasterDao().findTotalByStatus(ETableStatus.FULL);

            return "print-kot.xhtml?faces-redirect=true";
        } catch (Exception e) {
            e.printStackTrace();
            return "print-kot.xhtml?faces-redirect=true";
        }
    }

    public void completeTransactionAndPrint() {
        try {
            TableMaster master = new TableMaster();

            for (TableTransaction table : tableTransactions) {
                System.out.println("KOT: " + table.getKotRemarks());
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
        try {
            TableMaster master = new TableMaster();

            for (TableTransaction table : tableTransactions) {
                table.setStatus("Completed");
                new TableTransactionDao().update(table);

                master = table.getTableMaster();
                master.setTableStatus(ETableStatus.VACANT);
                tableDao.update(master);

//                payment.setTableTransaction(table);
                if (paymentMode.equalsIgnoreCase("CASH")) {
                    payment.setPaymentMode(EPaymentMode.CASH);
                } else if (paymentMode.equalsIgnoreCase("CARD")) {
                    payment.setPaymentMode(EPaymentMode.CARD);
                } else if (paymentMode.equalsIgnoreCase("CREDIT")) {
                    payment.setPaymentMode(EPaymentMode.CREDIT);
                } else if (paymentMode.equalsIgnoreCase("NC")) {
                    payment.setPaymentMode(EPaymentMode.NC);
                } else if (paymentMode.equalsIgnoreCase("MOBILEMONEY")) {
                    payment.setPaymentMode(EPaymentMode.MOBILEMONEY);
                } else if (paymentMode.equalsIgnoreCase("POSTTOROOM")) {
                    payment.setPaymentMode(EPaymentMode.POSTTOROOM);
                }

                payment.setMobileNumber(payerNumber);
                payment.setPaymentDate(new Date());
                new PaymentDao().register(payment);
                payment = new Payment();

            }

            tableTransactions = new TableTransactionDao().findByTableAndStatus(chosenTableMaster, "Completed");

            tableMasters = new TableMasterDao().findByType("Table");

            roomMasters = new TableMasterDao().findByType("Room");
            vipRoomMasters = new TableMasterDao().findByType("VipRoom");

            availableTable = new TableMasterDao().findTotalByStatus(ETableStatus.VACANT);
            billedTable = new TableMasterDao().findTotalByStatus(ETableStatus.BILLED);
            occupiedTable = new TableMasterDao().findTotalByStatus(ETableStatus.FULL);

            FacesContext fc = FacesContext.getCurrentInstance();
            fc.addMessage(null, new FacesMessage("Payment Saved"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void searchItems() {
        try {
            if (itemSearchKeyWord.isEmpty() || itemSearchKeyWord == null) {
                suggestedItems = new ArrayList<>();
            } else {
                suggestedItems = new ItemDao().findLikeName(itemSearchKeyWord.toUpperCase());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void refreshTables() {

        dailyCollection = new TableTransactionDao().findTotalByDate(new Date());
        dailyBilled = new TableTransactionDao().findTotalByDateAndTableStatus(new Date(), "Billed");

        availableTable = new TableMasterDao().findTotalByStatus(ETableStatus.VACANT);
        billedTable = new TableMasterDao().findTotalByStatus(ETableStatus.BILLED);
        occupiedTable = new TableMasterDao().findTotalByStatus(ETableStatus.FULL);

    }

    public void chooseItem(Item it) {
        itemChosen = it;
        suggestedItems = new ArrayList<>();
        itemSearchKeyWord = itemChosen.getItemName();
    }

    public void calculateTotalPrice() {
        totalPrice = purchasePrice * quantity;
    }
    
    
    public void itemUpdate() {
        try {
            if(unitPrice <= 0 || quantity <= 0 || purchasePrice <= 0 || unitPrice == null || quantity == null || purchasePrice == null){
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Fill Appropriate values"));
            }
            itemChosen.setQuantity(itemChosen.getQuantity()+quantity);
            itemChosen.setUnitRate(unitPrice);            
            new ItemDao().update(itemChosen);
            
            Purchase p = new Purchase();
            p.setQuantity(quantity);
            p.setPurchaseDate(new Date());
            p.setPurchasePrice(purchasePrice);
            p.setItem(itemChosen);
            p.setType("Stock");
            new PurchaseDao().register(p);
            
            Stock stock = new StockDao().findByDateAndItem(new Date(), itemChosen);
            if(stock == null){
                Stock st = new Stock();
                st.setEntryQuantity(quantity);
                st.setFinalStock(quantity);
                st.setInitialStock(quantity);
                st.setItem(itemChosen);
                st.setStockDate(new Date());
                st.setTotalSale(0.0);
                new StockDao().register(st);
            }else{
                
                stock.setEntryQuantity(quantity);
                stock.setFinalStock(stock.getFinalStock() + quantity);
                new StockDao().update(stock);
            }
            
            items = new ItemDao().findAll(Item.class);
            itemChosen = new Item();
            unitPrice = 0.0;
            purchasePrice = 0.0;
            quantity = 0.0;
            itemSearchKeyWord = "";

            FacesContext fc = FacesContext.getCurrentInstance();
            fc.addMessage(null, new FacesMessage("Item Stock updated"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


//    public void generateDailySalesReport() throws FileNotFoundException, DocumentException, BadElementException, IOException, Exception {
//
//        FacesContext context = FacesContext.getCurrentInstance();
//        Document document = new Document();
//        Rectangle rect = new Rectangle(20, 20, 580, 500);
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        PdfWriter writer = PdfWriter.getInstance((com.lowagie.text.Document) document, baos);
//        writer.setBoxSize("art", rect);
//        document.setPageSize(rect);
//        if (!document.isOpen()) {
//            document.open();
//        }
////        String path = FacesContext.getCurrentInstance().getExternalContext().getRealPath("\\uploads");
////        path = path.substring(0, path.indexOf("\\build"));
////        path = path + "\\web\\uploads\\hotel-logo\\" + hotel.getLogo();
////        Image image = Image.getInstance(path);
////        image.scaleAbsolute(50, 50);
////        image.setAlignment(Element.ALIGN_LEFT);
//        Paragraph title = new Paragraph();
//        //BEGIN page
////        title.add(image);
//        document.add(title);
//        com.lowagie.text.Font font0 = new com.lowagie.text.Font(com.lowagie.text.Font.TIMES_ROMAN, 9, com.lowagie.text.Font.NORMAL);
//        com.lowagie.text.Font font1 = new com.lowagie.text.Font(com.lowagie.text.Font.TIMES_ROMAN, 14, com.lowagie.text.Font.ITALIC, new Color(37, 46, 158));
//        com.lowagie.text.Font font2 = new com.lowagie.text.Font(com.lowagie.text.Font.TIMES_ROMAN, 9, com.lowagie.text.Font.NORMAL, new Color(0, 0, 0));
//        com.lowagie.text.Font font5 = new com.lowagie.text.Font(com.lowagie.text.Font.TIMES_ROMAN, 10, com.lowagie.text.Font.ITALIC, new Color(0, 0, 0));
//        com.lowagie.text.Font colorFont = new com.lowagie.text.Font(com.lowagie.text.Font.TIMES_ROMAN, 10, com.lowagie.text.Font.BOLD, new Color(0, 0, 0));
//        com.lowagie.text.Font font6 = new com.lowagie.text.Font(com.lowagie.text.Font.TIMES_ROMAN, 9, com.lowagie.text.Font.NORMAL);
//        document.add(new Paragraph("Rebero Resort\n"));
//        document.add(new Paragraph("KG 625 ST 4\n", font0));
//        document.add(new Paragraph("P.O.BOX 131 \n", font0));
//        document.add(new Paragraph("KIGALI-RWANDA\n\n", font0));
//        Paragraph p = new Paragraph("Daily Sales Report ", colorFont);
//        p.setAlignment(Element.ALIGN_CENTER);
//        document.add(p);
//        document.add(new Paragraph("\n"));
//        PdfPTable tables = new PdfPTable(7);
//        tables.setWidthPercentage(100);
//
//        PdfPCell cell1 = new PdfPCell(new Phrase("No", font2));
//        cell1.setBorder(Rectangle.BOX);
//        tables.addCell(cell1);
//
//        PdfPCell c2 = new PdfPCell(new Phrase("Table No", font2));
//        c2.setBorder(Rectangle.BOX);
//        tables.addCell(c2);
//
//        PdfPCell c3 = new PdfPCell(new Phrase("Payment Mode", font2));
//        c3.setBorder(Rectangle.BOX);
//        tables.addCell(c3);
//
////        PdfPCell c4 = new PdfPCell(new Phrase("Food", font2));
////        c4.setBorder(Rectangle.BOX);
////        tables.addCell(c4);
////
////        PdfPCell c5 = new PdfPCell(new Phrase("Beverage", font2));
////        c5.setBorder(Rectangle.BOX);
////        tables.addCell(c5);
////
////        PdfPCell c6 = new PdfPCell(new Phrase("Cigarette", font2));
////        c6.setBorder(Rectangle.BOX);
////        tables.addCell(c6);
//
//        PdfPCell c7 = new PdfPCell(new Phrase("Food", font2));
//        c7.setBorder(Rectangle.BOX);
//        tables.addCell(c7);
//
//        PdfPCell c8 = new PdfPCell(new Phrase("Beverage", font2));
//        c8.setBorder(Rectangle.BOX);
//        tables.addCell(c8);
//
//        PdfPCell c9 = new PdfPCell(new Phrase("Cigarette", font2));
//        c9.setBorder(Rectangle.BOX);
//        tables.addCell(c9);
//
//        PdfPCell c10 = new PdfPCell(new Phrase("Kot Remarks", font2));
//        c10.setBorder(Rectangle.BOX);
//        tables.addCell(c10);
//
//        tables.setHeaderRows(1);
//        PdfPCell pdfc5;
//        PdfPCell pdfc1;
//        PdfPCell pdfc3;
//        PdfPCell pdfc2;
//        PdfPCell pdfc4;
//        PdfPCell pdfc6;
//        PdfPCell pdfc7;
//        PdfPCell pdfc8;
//        PdfPCell pdfc9;
//        PdfPCell pdfc10;
//        int i = 1;
//        DecimalFormat dcf = new DecimalFormat("###,###,###");
//        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
//        Double cash = 0.0;
//        Double card = 0.0;
//        Double momo = 0.0;
//        Double credit = 0.0;
//        Double postToRoom = 0.0;
//        Double nc = 0.0;
//        Double totalFood = 0.0;
//        Double totalBeverage = 0.0;
//        
//        for (Payment x : new PaymentDao().findByTransactionDate(chosenDate)) {
//            
//            pdfc5 = new PdfPCell(new Phrase(i + "", font6));
//            pdfc5.setBorder(Rectangle.BOX);
//            tables.addCell(pdfc5);
//
//            pdfc4 = new PdfPCell(new Phrase(x.getTableTransaction().getTableMaster().getTableNo() + "", font6));
//            pdfc4.setBorder(Rectangle.BOX);
//            tables.addCell(pdfc4);
//
//            pdfc3 = new PdfPCell(new Phrase(x.getPaymentMode() + "", font6));
//            pdfc3.setBorder(Rectangle.BOX);
//            tables.addCell(pdfc3);
//
////            if (x.getPaymentMode().toString().equalsIgnoreCase("CREDIT")) {
////                System.out.println("True");
////
////                pdfc7 = new PdfPCell(new Phrase(0 + "", font6));
////                pdfc7.setBorder(Rectangle.BOX);
////                tables.addCell(pdfc7);
////
////                pdfc8 = new PdfPCell(new Phrase(0 + "", font6));
////                pdfc8.setBorder(Rectangle.BOX);
////                tables.addCell(pdfc8);
////
////                pdfc9 = new PdfPCell(new Phrase(0 + "", font6));
////                pdfc9.setBorder(Rectangle.BOX);
////                tables.addCell(pdfc9);
////
////                if (x.getTableTransaction().getItem().getMenuType().equalsIgnoreCase("Food")) {
////                    pdfc2 = new PdfPCell(new Phrase(x.getTableTransaction().getItem().getUnitRate() * x.getTableTransaction().getQuantity() + "", font6));
////                    pdfc2.setBorder(Rectangle.BOX);
////                    tables.addCell(pdfc2);
////
////                    pdfc1 = new PdfPCell(new Phrase(0 + "", font6));
////                    pdfc1.setBorder(Rectangle.BOX);
////                    tables.addCell(pdfc1);
////
////                    pdfc6 = new PdfPCell(new Phrase(0 + "", font6));
////                    pdfc6.setBorder(Rectangle.BOX);
////                    tables.addCell(pdfc6);
////
////                } else if (x.getTableTransaction().getItem().getMenuType().equalsIgnoreCase("Beverage")) {
////                    pdfc1 = new PdfPCell(new Phrase(x.getTableTransaction().getItem().getUnitRate() * x.getTableTransaction().getQuantity() + "", font6));
////                    pdfc1.setBorder(Rectangle.BOX);
////                    tables.addCell(pdfc1);
////
////                    pdfc2 = new PdfPCell(new Phrase(0 + "", font6));
////                    pdfc2.setBorder(Rectangle.BOX);
////                    tables.addCell(pdfc2);
////
////                    pdfc6 = new PdfPCell(new Phrase(0 + "", font6));
////                    pdfc6.setBorder(Rectangle.BOX);
////                    tables.addCell(pdfc6);
////
//////                } else {
////                    pdfc1 = new PdfPCell(new Phrase(0 + "", font6));
////                    pdfc1.setBorder(Rectangle.BOX);
////                    tables.addCell(pdfc1);
////
////                    pdfc2 = new PdfPCell(new Phrase(0 + "", font6));
////                    pdfc2.setBorder(Rectangle.BOX);
////                    tables.addCell(pdfc2);
////
////                    pdfc6 = new PdfPCell(new Phrase(x.getTableTransaction().getItem().getUnitRate() * x.getTableTransaction().getQuantity() + "", font6));
////                    pdfc6.setBorder(Rectangle.BOX);
////                    tables.addCell(pdfc6);
//////                }
////
//////            } else {
//                System.out.println("False");
////                pdfc2 = new PdfPCell(new Phrase(0 + "", font6));
////                pdfc2.setBorder(Rectangle.BOX);
////                tables.addCell(pdfc2);
////
////                pdfc1 = new PdfPCell(new Phrase(0 + "", font6));
////                pdfc1.setBorder(Rectangle.BOX);
////                tables.addCell(pdfc1);
////
////                pdfc6 = new PdfPCell(new Phrase(0 + "", font6));
////                pdfc6.setBorder(Rectangle.BOX);
////                tables.addCell(pdfc6);
//
//                if (x.getTableTransaction().getItem().getMenuType().equalsIgnoreCase("Food")) {
//                    System.out.println("Menu Type----"+x.getTableTransaction().getItem().getMenuType());
//                    
//                    
//                    pdfc7 = new PdfPCell(new Phrase(x.getTableTransaction().getItem().getUnitRate() * x.getTableTransaction().getQuantity() + "", font6));
//                    pdfc7.setBorder(Rectangle.BOX);
//                    tables.addCell(pdfc7);
//
//                    pdfc8 = new PdfPCell(new Phrase(0+"", font6));
//                    pdfc8.setBorder(Rectangle.BOX);
//                    tables.addCell(pdfc8);
//
//                    pdfc9 = new PdfPCell(new Phrase(0+"", font6));
//                    pdfc9.setBorder(Rectangle.BOX);
//                    tables.addCell(pdfc9);
//                } else if (x.getTableTransaction().getItem().getMenuType().equalsIgnoreCase("Beverage")) {
//                    System.out.println("Menu Type----"+x.getTableTransaction().getItem().getMenuType()+x.getTableTransaction().getTotalPrice());
//                    
//                    pdfc7 = new PdfPCell(new Phrase(0+"", font6));
//                    pdfc7.setBorder(Rectangle.BOX);
//                    tables.addCell(pdfc7);
//                    
//                    pdfc8 = new PdfPCell(new Phrase(x.getTableTransaction().getItem().getUnitRate() * x.getTableTransaction().getQuantity() + "", font6));
//                    pdfc8.setBorder(Rectangle.BOX);
//                    tables.addCell(pdfc8);
//
//
//                    pdfc9 = new PdfPCell(new Phrase(0+"", font6));
//                    pdfc9.setBorder(Rectangle.BOX);
//                    tables.addCell(pdfc9);
//
//                } else {
//                    pdfc7 = new PdfPCell(new Phrase(0 + "", font6));
//                    pdfc7.setBorder(Rectangle.BOX);
//                    tables.addCell(pdfc7);
//
//                    pdfc8 = new PdfPCell(new Phrase(0 + "", font6));
//                    pdfc8.setBorder(Rectangle.BOX);
//                    tables.addCell(pdfc8);
//
//                    pdfc9 = new PdfPCell(new Phrase(x.getTableTransaction().getItem().getUnitRate() * x.getTableTransaction().getQuantity() + "", font6));
//                    pdfc9.setBorder(Rectangle.BOX);
//                    tables.addCell(pdfc9);
//                }
////            }
//            pdfc10 = new PdfPCell(new Phrase(x.getTableTransaction().getKotRemarks() + "", font6));
//            pdfc10.setBorder(Rectangle.BOX);
//            tables.addCell(pdfc10);
//
//            if(x.getPaymentMode().toString().equalsIgnoreCase("CASH")){
//                cash = cash + x.getTableTransaction().getTotalPrice();
//            }else if(x.getPaymentMode().toString().equalsIgnoreCase("CARD")){
//                card = card + x.getTableTransaction().getTotalPrice();
//            }else if(x.getPaymentMode().toString().equalsIgnoreCase("MOBILEMONEY")){
//                momo = momo + x.getTableTransaction().getTotalPrice();
//            }else if(x.getPaymentMode().toString().equalsIgnoreCase("CREDIT")){
//                credit = credit + x.getTableTransaction().getTotalPrice();
//            }else if(x.getPaymentMode().toString().equalsIgnoreCase("POST TO ROOM")){
//                postToRoom = postToRoom + x.getTableTransaction().getTotalPrice();
//            }else if(x.getPaymentMode().toString().equalsIgnoreCase("NC")){
//                nc = nc + x.getTableTransaction().getTotalPrice();
//            }
//            
//            if(x.getPaymentMode().toString().equalsIgnoreCase("CASH") && x.getTableTransaction().getItem().getMenuType().equalsIgnoreCase("Food")){
//                totalFood = totalFood + x.getTableTransaction().getTotalPrice();
//            }else if(x.getPaymentMode().toString().equalsIgnoreCase("CARD") && x.getTableTransaction().getItem().getMenuType().equalsIgnoreCase("Food")){
//                totalFood = totalFood + x.getTableTransaction().getTotalPrice();
//            }else if(x.getPaymentMode().toString().equalsIgnoreCase("MOBILEMONEY") && x.getTableTransaction().getItem().getMenuType().equalsIgnoreCase("Food")){
//                totalFood = totalFood + x.getTableTransaction().getTotalPrice();
//            }
//            
//            if(x.getPaymentMode().toString().equalsIgnoreCase("CASH") && x.getTableTransaction().getItem().getMenuType().equalsIgnoreCase("Beverage")){
//                totalBeverage = totalBeverage + x.getTableTransaction().getTotalPrice();
//            }else if(x.getPaymentMode().toString().equalsIgnoreCase("CARD") && x.getTableTransaction().getItem().getMenuType().equalsIgnoreCase("Beverage")){
//                totalBeverage = totalBeverage + x.getTableTransaction().getTotalPrice();
//            }else if(x.getPaymentMode().toString().equalsIgnoreCase("MOBILEMONEY") && x.getTableTransaction().getItem().getMenuType().equalsIgnoreCase("Beverage")){
//                totalBeverage = totalBeverage + x.getTableTransaction().getTotalPrice();
//            }
//            
//            i++;
//        }
//        
//        
//        PdfPTable tables1 = new PdfPTable(4);
//        tables1.setWidthPercentage(100);
//        
//        PdfPCell ce1 = new PdfPCell(new Phrase("CASH", font2));
//        ce1.setBorder(Rectangle.BOX);
//        tables1.addCell(ce1);
//
//        PdfPCell ce2 = new PdfPCell(new Phrase(cash+"", font2));
//        ce2.setBorder(Rectangle.BOX);
//        tables1.addCell(ce2);
//
//        PdfPCell cee1 = new PdfPCell(new Phrase("TOTAL FOOD(CASH + CARD + MOBILE)", font2));
//        cee1.setBorder(Rectangle.BOX);
//        tables1.addCell(cee1);
//        
//        PdfPCell cee2 = new PdfPCell(new Phrase(totalFood+"", font2));
//        cee2.setBorder(Rectangle.BOX);
//        tables1.addCell(cee2);
//        
//        PdfPCell ce3 = new PdfPCell(new Phrase("CARD", font2));
//        ce3.setBorder(Rectangle.BOX);
//        tables1.addCell(ce3);
//
//        PdfPCell ce4 = new PdfPCell(new Phrase(card+"", font2));
//        ce4.setBorder(Rectangle.BOX);
//        tables1.addCell(ce4);
//
//        PdfPCell ce5 = new PdfPCell(new Phrase("TOTAL BEVERAGE(CASH + CARD + MOBILE)", font2));
//        ce5.setBorder(Rectangle.BOX);
//        tables1.addCell(ce5);
//        
//        PdfPCell ce6 = new PdfPCell(new Phrase(totalBeverage+"", font2));
//        ce6.setBorder(Rectangle.BOX);
//        tables1.addCell(ce6);
//
//        PdfPCell ce7 = new PdfPCell(new Phrase("MOBILE MONEY", font2));
//        ce7.setBorder(Rectangle.BOX);
//        tables1.addCell(ce7);
//
//        PdfPCell ce8 = new PdfPCell(new Phrase(momo+"", font2));
//        ce8.setBorder(Rectangle.BOX);
//        tables1.addCell(ce8);
//
//        PdfPCell ce9 = new PdfPCell(new Phrase("TOTAL CIGARETTE(CASH + CARD + MOBILE)", font2));
//        ce9.setBorder(Rectangle.BOX);
//        tables1.addCell(ce9);
//        
//        PdfPCell ce10 = new PdfPCell(new Phrase(0+"", font2));
//        ce10.setBorder(Rectangle.BOX);
//        tables1.addCell(ce10);
//        
//        PdfPCell ce11 = new PdfPCell(new Phrase("CREDIT", font2));
//        ce11.setBorder(Rectangle.BOX);
//        tables1.addCell(ce11);
//
//        PdfPCell ce12 = new PdfPCell(new Phrase(credit+"", font2));
//        ce12.setBorder(Rectangle.BOX);
//        tables1.addCell(ce12);
//
//        PdfPCell ce13 = new PdfPCell(new Phrase("", font2));
//        ce13.setBorder(Rectangle.BOX);
//        tables1.addCell(ce13);
//        
//        PdfPCell ce14 = new PdfPCell(new Phrase("", font2));
//        ce14.setBorder(Rectangle.BOX);
//        tables1.addCell(ce14);
//
//        
//        PdfPCell ce15 = new PdfPCell(new Phrase("POST TO ROOM", font2));
//        ce15.setBorder(Rectangle.BOX);
//        tables1.addCell(ce15);
//
//        PdfPCell ce16 = new PdfPCell(new Phrase(postToRoom+"", font2));
//        ce16.setBorder(Rectangle.BOX);
//        tables1.addCell(ce16);
//
//        PdfPCell ce17 = new PdfPCell(new Phrase("", font2));
//        ce17.setBorder(Rectangle.BOX);
//        tables1.addCell(ce17);
//        
//        PdfPCell ce18 = new PdfPCell(new Phrase("", font2));
//        ce18.setBorder(Rectangle.BOX);
//        tables1.addCell(ce18);
//        
//        PdfPCell ce19 = new PdfPCell(new Phrase("NC", font2));
//        ce19.setBorder(Rectangle.BOX);
//        tables1.addCell(ce19);
//
//        PdfPCell ce20 = new PdfPCell(new Phrase(nc+"", font2));
//        ce20.setBorder(Rectangle.BOX);
//        tables1.addCell(ce20);
//
//        PdfPCell ce26 = new PdfPCell(new Phrase("", font2));
//        ce26.setBorder(Rectangle.BOX);
//        tables1.addCell(ce26);
//        
//        PdfPCell ce21 = new PdfPCell(new Phrase("", font2));
//        ce21.setBorder(Rectangle.BOX);
//        tables1.addCell(ce21);
//        
//        PdfPCell ce27 = new PdfPCell(new Phrase("Total", font2));
//        ce27.setBorder(Rectangle.BOX);
//        tables1.addCell(ce27);
//        
//        PdfPCell ce22 = new PdfPCell(new Phrase((cash+credit+momo+nc+postToRoom+card)+"", font2));
//        ce22.setBorder(Rectangle.BOX);
//        tables1.addCell(ce22);
//        
//        
//        PdfPCell ce23 = new PdfPCell(new Phrase("TOTAL (CASH + CARD + MOBILE)", font2));
//        ce23.setBorder(Rectangle.BOX);
//        tables1.addCell(ce23);
//        
//        PdfPCell ce24 = new PdfPCell(new Phrase((totalFood+totalBeverage)+"", font2));
//        ce24.setBorder(Rectangle.BOX);
//        tables1.addCell(ce24);
//
//        document.add(tables);
//        document.add(Chunk.NEWLINE);
//        document.add(tables1);
//        Paragraph par = new Paragraph("\n\nPrinted On: " + sdf.format(new Date()) + ". By: " + loggedInUser.getNames(), font1);
//        par.setAlignment(Element.ALIGN_RIGHT);
//        document.add(par);
//        document.close();
//        String fileName = "Report_" + new Date().getTime() / (1000 * 3600 * 24);
//        writePDFToResponse(context.getExternalContext(), baos, fileName);
//        context.responseComplete();
//    }
    private void writePDFToResponse(ExternalContext externalContext, ByteArrayOutputStream baos, String fileName) throws IOException {
        externalContext.responseReset();
        externalContext.setResponseContentType("application/pdf");
        externalContext.setResponseHeader("Expires", "0");
        externalContext.setResponseHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
        externalContext.setResponseHeader("Pragma", "public");
        externalContext.setResponseHeader("Content-disposition", "attachment;filename=" + fileName + ".pdf");
        externalContext.setResponseContentLength(baos.size());
        OutputStream out = externalContext.getResponseOutputStream();
        baos.writeTo(out);
        externalContext.responseFlushBuffer();
    }

    public Person getLoggedInUser() {
        return loggedInUser;
    }

    public void setLoggedInUser(Person loggedInUser) {
        this.loggedInUser = loggedInUser;
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

    public List<Person> getWaiters() {
        return waiters;
    }

    public void setWaiters(List<Person> waiters) {
        this.waiters = waiters;
    }

    public UserDepartment getWaitery() {
        return waitery;
    }

    public void setWaitery(UserDepartment waitery) {
        this.waitery = waitery;
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

    public HotelConfig getHotel() {
        return hotel;
    }

    public void setHotel(HotelConfig hotel) {
        this.hotel = hotel;
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

    public static String[] getTitle() {
        return title;
    }

    public static void setTitle(String[] title) {
        RestaurantModel.title = title;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public static String[] getKotTitle() {
        return kotTitle;
    }

    public static void setKotTitle(String[] kotTitle) {
        RestaurantModel.kotTitle = kotTitle;
    }

    public List<TableMaster> getRoomMasters() {
        return roomMasters;
    }

    public void setRoomMasters(List<TableMaster> roomMasters) {
        this.roomMasters = roomMasters;
    }

    public List<TableMaster> getVipRoomMasters() {
        return vipRoomMasters;
    }

    public void setVipRoomMasters(List<TableMaster> vipRoomMasters) {
        this.vipRoomMasters = vipRoomMasters;
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

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public Double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Double getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(Double purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

}
