package common;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import dao.BookingDao;
import dao.PaymentDao;
import dao.TableTransactionDao;
import domain.Booking;
import domain.Payment;
import dto.FrontOfficeCollectionDto;
import dto.TableTransactionDto;
import enums.EType;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import uimodel.FrontOfficeModel;

public class Test {

    public static void main(String[] args) throws Exception {

//        HibernateUtil.getSessionFactory().openSession();
//        HibernateUtil.getSessionFactory().close();
//        UserDepartment dep = new UserDepartment();
//        dep.setDepartmentName("ADMINISTRATOR");
//        dep.setStatus(EStatus.ACTIVE);
//        new UserDepartmentDao().register(dep);        
//        UserDepartment dep = new UserDepartmentDao().findOne(UserDepartment.class, "6fbcc92c-30af-4f5f-8509-93f31819de07");
//        Person u = new Person();
//        u.setUserDepartment(dep);
//        u.setNames("Kalisa");
//        u.setPhone("0788909884");
//        u.setUsername("admin");
//        u.setPassword(new PassCode().encrypt("admin"));
//        u.setStatus(EStatus.ACTIVE);
//        new PersonDao().register(u);
//        
//        Account a = new Account();
//        a.setPerson(u);
//        a.setUsername("cashier");
//        a.setPassword(new PassCode().encrypt("cashier"));
//        a.setStatus(EStatus.ACTIVE);
//        new AccountDao().register(a);
//        HotelConfig h = new HotelConfig();
//        h.setAddressLine1("Kigali-Rwanda");
//        h.setAddressLine2("Kimihurura");
//        h.setAddressLine3("Rugando");
//        h.setCurrency("RWF");
//        h.setEmail("hotel@gmail.com");
//        h.setHotelName("Hotel Test");
//        h.setSlogan("Slogan Test");
//        h.setStreetNo("KK. 324 St");
//        new HotelConfigDao().register(h);
//        List<TableMaster> list = new TableTransactionDao().findByTableStatus(ETableStatus.FULL, "Sent");
//        for(TableMaster t: list){
//            System.out.println(t.getTableNo());
//        }
//        for(TableTransaction tr: new TableTransactionDao().findAll(TableTransaction.class)){
//            tr.setTotalPrice(tr.getQuantity() * tr.getItem().getUnitRate());
//            new TableTransactionDao().update(tr);
//        }
//        for(TableMaster tm: new TableTransactionDao().findByTableStatus(ETableStatus.BILLED, "Sent")){
//            System.out.println(tm.getTableNo());
//        }
//        new RestaurantModel().generateDailySalesReport();
//        List<TableGroup> tg = new TableGroupDao().findAll(TableGroup.class);
//        for(TableMaster t: new TableMasterDao().findAll(TableMaster.class)){
//            t.setType("Table");
//            new TableMasterDao().update(t);
//        }
//        new RestaurantModel1().generateDailySalesReport();
//        for(TableTransaction t: new TableTransactionDao().findByStatusAndDate("Completed", new Date())){
//            System.out.println(t.getItem().getItemName());
//        }
//        
//        System.out.println(new TableTransactionDao().findTotalByDateAndTableStatus(new Date(), "Completed"));
//            for(Purchase p:  new PurchaseDao().findItemsPurchasedByDateAndType(new Date(), "Expense")){
//                System.out.println(p.getPurchasePrice());
//            }
//        Date from = new SimpleDateFormat("dd/MM/yyyy").parse("22/12/2020");
//        Date to = new SimpleDateFormat("dd/MM/yyyy").parse("22/12/2021");
//        Person p = new PersonDao().findOne(Person.class, "36ba2705-4daa-4d62-aa10-adddf1058582");
//        System.out.println("Test---"+p.getNames());
//        for(TableTransaction t: new TableTransactionDao().findByStatusAndDateAndPerson("completed", from, p)){
//            System.out.println("Test---"+t.getTransactionId());
//        }
        /**
         * Load the hibernate.cfg.xml from the classpath*
         */
//        Configuration cfg = new Configuration();
//        cfg.setProperty("hibernate.connection.password", "root1");
//        if (new Date().compareTo(new SimpleDateFormat("dd/MM/yyyy").parse("16/02/2021")) == 0) {
//            Configuration cfg = new Configuration();
//            cfg.setProperty("hibernate.connection.password", "root1");
//            SessionFactory sessionFactory = cfg.buildSessionFactory();
//        }
//        System.out.println("Date 1 = " + new Date());
//        System.out.println("Date 2 = " + new SimpleDateFormat("dd/MM/yyyy").parse("16/02/2021"));
//        System.out.println("Comparison = " + new Date().compareTo(new SimpleDateFormat("dd/MM/yyyy").parse("16/01/2021")));
//        new SimpleDateFormat
//        if(new Date().compareTo(new Date(2021,02,16)) == 0){
//            System.out.println("Teest Passed");
//        }
//        SessionFactory sessionFactory = cfg.buildSessionFactory();
//        Date from = new SimpleDateFormat("dd/MM/yyyy").parse("22/12/2020");
//        Date to = new SimpleDateFormat("dd/MM/yyyy").parse("22/12/2022");
//        for(TableTransactionDto t: new TableTransactionDao().findTransactionDetailsByDateBetween("Completed", from, to)){
//            System.out.println("Item ID-- "+t.getItemName()+" --- Quantity-- "+t.getQuantity());
//        }
//        Timer t = new Timer();
//        MyTask mTask = new MyTask();
//        // This task is scheduled to run every 10 seconds
//
//        t.scheduleAtFixedRate(mTask, 0, 10000);
//        Date from = new DateTime(new Date()).plusDays(1).toDate();
//
//        System.out.println(new StockDao().isStockAvailable(from));

//        Item i = new ItemDao().findOne(Item.class, "3384d0df-febe-4433-8b89-cc259ab4458d");

//for (TableTransactionDto t : new TableTransactionDao().findTransactionDetailsByDateBetween("Completed", from, from)) {
//    System.out.println(t.getItemName()+" "+t.getQuantity());
//                    if (t.getItemName().equalsIgnoreCase(i.getItemName())) {
//                        System.out.println(t.getQuantity());
//                    }
//                }
//        Stock stock = new StockDao().findByItem( i);
//        System.out.println(stock);
//if(new PurchaseDao().findByItemAndDate(from, i) != null) {
//            System.out.println(new PurchaseDao().findByItemAndDate(from, i).getQuantity());
//        }

//        Double total = 0.0;
//        for(Payment p: new PaymentDao().findByStatusAndDateBetween("Completed", new Date(), new Date())){
//            total = total + p.getDiscount();
//        }
//        
//        System.out.println("Total = " + total);


//        for(Payment p: new PaymentDao().findByStatusAndDateBetweenAndMobileNumber("Completed", new Date(), new Date())){
//            System.out.println(p.getMobileNumber());
//        }
//        for(Payment p: new PaymentDao().findByTransactionDateAndCreditAndPerson(new Date(), "Mandela")){
//            System.out.println(p.getMobileNumber() +" "+p.getAmountPaidCredit());
//        }
//        System.out.println("Hotel = "+new HotelConfigDao().findOne(HotelConfig.class, Long.parseLong("1")).getHotelName());

        Date from = new SimpleDateFormat("dd/MM/yyyy hh:mm").parse("31/07/2021 00:30");
        Date to = new SimpleDateFormat("dd/MM/yyyy hh:mm").parse("31/07/2022 23:00");
//        
//        for(TableTransactionDto t: new TableTransactionDao().findTransactionDetailsByDateAndHourBetween("Completed", from, to)){
//            System.out.println(t.getQuantity());
//        }

//         Rectangle small = new Rectangle(292, 720);
//        Font smallfont = new Font(Font.HELVETICA, 10);
//        Font boldfont = new Font(Font.HELVETICA, 10, Font.BOLD);
//
//        Document document = new Document(small, 5, 5, 15, 5);
//        PdfWriter.getInstance(document, new FileOutputStream("C:\\"));
//        document.open();
//        PdfPTable table = new PdfPTable(2);
//        table.setTotalWidth(new float[]{ 160, 120 });
//        table.setLockedWidth(true);
//
//// first row
//        PdfPCell cell;
//        cell = new PdfPCell(new Phrase("item1", boldfont));
//        cell.setVerticalAlignment(Element.ALIGN_CENTER);
//        cell.setBorder(Rectangle.LEFT);
//        cell.setPadding(5);
//        table.addCell(cell);
//        cell = new PdfPCell(new Phrase("item2", smallfont));
//        cell.setVerticalAlignment(Element.ALIGN_CENTER);
//        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//        cell.setBorder(Rectangle.LEFT);
//        cell.setBorder(Rectangle.RIGHT);
//        cell.setPadding(5);
//        table.addCell(cell);
//
//        for(Payment t : new PaymentDao().findByStatusAndDateBetweenAndNotRoomPayment("Completed", from, to)){
//            System.out.println(t.getAmountPaid());
//        }

//        System.out.println(new PaymentDao().findByBookingAndType(new BookingDao().findOne(Booking.class, "6dcbd9a1-efb3-400a-9bed-72f3c2eb8bfb"), EType.ROOM));
        
        for(FrontOfficeCollectionDto f: new BookingDao().findByDateBetweenAndPaidGroup(from, to)){
            System.out.println(f.getAmount()+"----"+f.getDiscount()+"----"+f.getGuestName()+"----"+f.getRoomNo()+"---"+f.getAmountPaidCash()+"---"+f.getAmountPaidMomo());
            
        }
    }
}
