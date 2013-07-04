import org.dbunit.IDatabaseTester;
import org.dbunit.JdbcDatabaseTester;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.h2.jdbcx.JdbcDataSource;
import org.h2.tools.RunScript;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.qsoft.dao.BankAccountDao;
import org.qsoft.dao.TransactionDao;
import org.qsoft.entity.BankAccount;
import org.qsoft.entity.Transaction;

import javax.sql.DataSource;
import java.nio.charset.Charset;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Created by IntelliJ IDEA.
 * User: haopt
 * Date: 7/4/13
 * Time: 1:41 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestDaoH {
    Date dateTimes = mock(Date.class);

    private static final String JDBC_DRIVER = org.h2.Driver.class.getName();
    private static final String JDBC_URL = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1";
    private static final String USER = "sa";
    private static final String PASSWORD = "";

    @BeforeClass
    public static void createSchema() throws Exception {
        String schemaFileName = System.class.getResource("/schema.sql").toString().substring(6);
        String transactionTable = System.class.getResource("/schemaTransactionTalbe.sql").toString().substring(6);

        RunScript.execute(JDBC_URL, USER, PASSWORD, schemaFileName, Charset.forName("UTF8"), false);
        RunScript.execute(JDBC_URL, USER, PASSWORD, transactionTable, Charset.forName("UTF8"), false);
    }

    @Before
    public void importDataSet() throws Exception {
        reset(dateTimes);

        IDataSet dataSet = readDataSet();  // read data from xml file
        cleanlyInsert(dataSet);  // empty the db and insert data
    }

    private IDataSet readDataSet() throws Exception {
        return new FlatXmlDataSetBuilder().build(System.class.getResource("/dataset.xml"));
    }

    private void cleanlyInsert(IDataSet dataSet) throws Exception {
        IDatabaseTester databaseTester = new JdbcDatabaseTester(JDBC_DRIVER, JDBC_URL, USER, PASSWORD);
        databaseTester.setSetUpOperation(DatabaseOperation.CLEAN_INSERT);
        databaseTester.setDataSet(dataSet);
        databaseTester.onSetup();
    }

    private DataSource dataSource() {
        JdbcDataSource dataSource = new JdbcDataSource();
        dataSource.setURL(JDBC_URL);
        dataSource.setUser(USER);
        dataSource.setPassword(PASSWORD);
        return dataSource;
    }

    @Test
    public void testFindByAccountNumber() throws Exception {
        BankAccountDao bankAccountDao = new BankAccountDao();
        bankAccountDao.setConnection(dataSource().getConnection());
        BankAccount bankAccount = bankAccountDao.findByAccountNumber("0123456789");
        assertEquals("0123456789", bankAccount.getAccountNumber());
    }

    @Test
    public void testInsertBankAccount() throws Exception {
        BankAccountDao bankAccountDao = new BankAccountDao();
        bankAccountDao.setConnection(dataSource().getConnection());

        BankAccount bankAccount = new BankAccount("123", "create new 123", 10000);
        BankAccount bankAccount1 = new BankAccount("124", "create new 124", 10000);
        bankAccountDao.insertBankAccount(bankAccount);
        bankAccountDao.insertBankAccount(bankAccount1);

        BankAccount bankAccountTestGet = bankAccountDao.findByAccountNumber("123");

        assertEquals("123", bankAccount.getAccountNumber());

        assertEquals(3, bankAccountDao.selectTotalRow());
    }

    private BankAccountDao setupBankAccountDao() throws Exception {
        BankAccountDao bankAccountDao = new BankAccountDao();
        bankAccountDao.setConnection(dataSource().getConnection());
        return bankAccountDao;
    }
    private TransactionDao setupTransactionDao() throws Exception {
        TransactionDao transactionDao = new TransactionDao();
        transactionDao.setConnection(dataSource().getConnection());
        return transactionDao;
    }

    @Test
    public void testDepositBankAccount() throws Exception {
        when(dateTimes.getTime()).thenReturn(1000L).thenReturn(2000L).thenReturn(3000L).thenReturn(4000L).thenReturn(5000L);

        BankAccountDao bankAccountDao = setupBankAccountDao();
        TransactionDao transactionDao = setupTransactionDao();

        BankAccount bankAccount = new BankAccount("123", "create new 123", 10000);
        bankAccountDao.insertBankAccount(bankAccount);

        //--------------------------------------check balance account just create
        BankAccount bankAccountTestGet = bankAccountDao.findByAccountNumber("123");
        assertEquals("10000.0", String.valueOf(bankAccountTestGet.getBalance()));         //true

        //--------------------------------------deposit 2000
        double valueDeposit = 2000;
        bankAccountDao.depositBankAccount(bankAccountTestGet, valueDeposit);
        BankAccount baCheckDeposit = bankAccountDao.findByAccountNumber("123");

        //--------------------------------------Insert Deposit Log
        Transaction tr = new Transaction(baCheckDeposit.getAccountNumber(), dateTimes.getTime(), valueDeposit, "Deposit 2000");
        transactionDao.insertTransaction(tr);

        assertEquals("12000.0", String.valueOf(baCheckDeposit.getBalance()));         //true

//        check log deposit.                luc nay bang transaction moi chi co 1 row cua depsit o tren
        assertEquals(1, transactionDao.selectTotalRow());         //true
    }

}
