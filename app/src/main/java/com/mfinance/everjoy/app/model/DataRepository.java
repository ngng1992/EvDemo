package com.mfinance.everjoy.app.model;

import android.os.Message;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.StringTokenizer;

import com.mfinance.everjoy.app.CompanySettings;

import com.mfinance.everjoy.R;
import com.mfinance.everjoy.app.bo.BalanceRecord;
import com.mfinance.everjoy.app.bo.CancelledOrder;
import com.mfinance.everjoy.app.bo.CashMovementRecord;
import com.mfinance.everjoy.app.bo.ContractObj;
import com.mfinance.everjoy.app.bo.ExecutedOrder;
import com.mfinance.everjoy.app.bo.LiquidationRecord;
import com.mfinance.everjoy.app.bo.OpenPositionRecord;
import com.mfinance.everjoy.app.bo.OrderRecord;
import com.mfinance.everjoy.app.bo.SystemMessage;
import com.mfinance.everjoy.app.bo.TransactionObj;
import com.mfinance.everjoy.app.constant.ServiceFunction;
import com.mfinance.everjoy.app.pojo.ConnectionStatus;
import com.mfinance.everjoy.app.util.MessageObj;
import com.mfinance.everjoy.app.util.PriceMessageObj;
import com.mfinance.everjoy.hungkee.xml.Advertisement;
import com.mfinance.everjoy.hungkee.xml.Advertisements;
import com.mfinance.everjoy.hungkee.xml.CompanyProfile;
import com.mfinance.everjoy.hungkee.xml.ContactUs;
import com.mfinance.everjoy.hungkee.xml.Economicdata;
import com.mfinance.everjoy.hungkee.xml.Economicdatas;
import com.mfinance.everjoy.hungkee.xml.Hourproduct;
import com.mfinance.everjoy.hungkee.xml.Hourproducts;
import com.mfinance.everjoy.hungkee.xml.Master;
import com.mfinance.everjoy.hungkee.xml.Masters;
import com.mfinance.everjoy.hungkee.xml.News;
import com.mfinance.everjoy.hungkee.xml.Newscontent;
import com.mfinance.everjoy.hungkee.xml.Newscontents;
import com.mfinance.everjoy.hungkee.xml.Newses;
import com.mfinance.everjoy.hungkee.xml.Other;
import com.mfinance.everjoy.hungkee.xml.Strategies;
import com.mfinance.everjoy.hungkee.xml.Strategy;

/**
 * It is a repository for storing model data  
 *
 *
 *  @author        : justin.lai
 *  @version       : v1.00
 *
 *
 *  @ModificationHistory
 *  Who			When			Version			What<br>
 *  -------------------------------------------------------------------------------<br> 
 *  justin.lai	20110413		v1.00			Creation<br>
 *
 *
 **
 */

public class DataRepository {
    /**
	 * User ID
	 */
	private String strUser;
	/**
	 * User passowrd
	 */
	private String strPassword;

	private String strEmail;
	/**
	 * Server URL
	 */
	private String strURL;
	/**
	 * Connection port
	 */
	private int iPort;
	/**
	 * Data repostory instance
	 */
	private final static DataRepository instance = new DataRepository();
	/**
	 * Account balance
	 */
	private BalanceRecord balance;
	/**
	 * HashMap for storing contract data
	 */
	private HashMap<String, ContractObj> hmContract = new HashMap<String, ContractObj>(20);

	public HashMap<String, ContractObj> hmContractBidAskAdjTemp = new HashMap<String, ContractObj>(20);

	/**
	 * ArrayList for storing contract data
	 */
	private ArrayList<ContractObj> alContract = new ArrayList<ContractObj>(20);

	private ArrayList<String> alContractSeq = new  ArrayList<String>(20);

	private List<String> alContractSeqSort = new  ArrayList<String>(20);

	private ArrayList<String> alContractDefaultSeq = new  ArrayList<String>(20);
	/**
	 * HashMap for storing open position object
	 */
	private HashMap<Integer, OpenPositionRecord> hmPosition = new HashMap<Integer, OpenPositionRecord>(20);
	/**
	 * ArrayList for storing open position object 
	 */
	private ArrayList<OpenPositionRecord> alPosition = new ArrayList<OpenPositionRecord>(20);
	/**
	 * HashMap for storing running order
	 */
	private Map<Integer, OrderRecord> hmRunningOrder = Collections.unmodifiableMap(Collections.EMPTY_MAP);
	/**
	 * ArrayList for storing running order
	 */
	private ArrayList<OrderRecord> alRunningOrder = new ArrayList<OrderRecord>(20);
	/**
	 * HashMap for storing executed order
	 */
	private HashMap<Integer, ExecutedOrder> hmExecutedOrder = new HashMap<Integer, ExecutedOrder>(20);
	/**
	 * ArrayList for storing executed order
	 */
	private ArrayList<ExecutedOrder> alExecutedOrder = new ArrayList<ExecutedOrder>(20);
	/**
	 * HashMap for storing canceled order
	 */
	private HashMap<Integer, CancelledOrder> hmCancelledOrder = new HashMap<Integer, CancelledOrder>(20);
	/**
	 * ArrayList for storing canceled order
	 */
	private ArrayList<CancelledOrder> alCancelledOrder = new ArrayList<CancelledOrder>(20);
	/**
	 * HashMap for storing liquidation history
	 */
	private HashMap<String, LiquidationRecord> hmLiquidationRecord = new HashMap<String, LiquidationRecord>(20);
	/**
	 * ArrayList for storing liquidation history
	 */
	private ArrayList<LiquidationRecord> alLiquidationRecord = new ArrayList<LiquidationRecord>(20);
	/**
	 * HashMap for storing CashMovement history
	 */
	private Map<String, CashMovementRecord> hmCashMovementRecord = new HashMap<String, CashMovementRecord>(20);
	/**
	 * ArrayList for storing CashMovement history
	 */
	private ArrayList<CashMovementRecord> alCashMovementRecord = new ArrayList<CashMovementRecord>(20);
	/**
	 * HashMap for storing transaction history
	 */
	private HashMap<String, TransactionObj> hmTransaction = new HashMap<String, TransactionObj>(20);
	/**
	 * ArrayList for storing transaction history
	 */
	private ArrayList<TransactionObj> alTransaction = new ArrayList<TransactionObj>(20);
	/**
	 * HashMap for storing system message
	 */
	private Map<Integer, SystemMessage> hmSysMsg = Collections.EMPTY_MAP;
	/**
	 * ArrayList for storing system message
	 */
	private ArrayList<SystemMessage> alSysMsg = new ArrayList<SystemMessage>(20);
	/**
	 * HashMap for storing a mapping between contract name and contract object
	 */
	private HashMap<String, ContractObj> hmContractName = new HashMap<String, ContractObj>();
	/**
	 * HashMap for storing a mapping between contract code and contract image
	 */
	private HashMap<String, int[]> hmContractImage = new HashMap<String, int[]>();

	private Map<String, String> depositTermsMap = Collections.EMPTY_MAP;
	private Map<String, String> withdrawalTermsMap = Collections.EMPTY_MAP;

	public static HashMap<String, Integer> hmCurrImageMap = new HashMap<String, Integer>();
	static{
		hmCurrImageMap.put("USD", R.drawable.flag_usd);
		hmCurrImageMap.put("AUD", R.drawable.flag_aud);
		hmCurrImageMap.put("NZD", R.drawable.flag_nzd);
		hmCurrImageMap.put("GBP", R.drawable.flag_gbp);
		hmCurrImageMap.put("JPY", R.drawable.flag_jpy);
		hmCurrImageMap.put("EUR", R.drawable.flag_eur);
		hmCurrImageMap.put("CAD", R.drawable.flag_cad);
		hmCurrImageMap.put("CHF", R.drawable.flag_chf);
		hmCurrImageMap.put("HKD", R.drawable.flag_hkd);
		hmCurrImageMap.put("USD", R.drawable.flag_usd);
		hmCurrImageMap.put("CNY", R.drawable.flag_rmb);
		hmCurrImageMap.put("RMB", R.drawable.flag_rmb);
		hmCurrImageMap.put("COZC", R.drawable.flag_corn);
		hmCurrImageMap.put("COZS", R.drawable.flag_soyabeans);
		hmCurrImageMap.put("COZW", R.drawable.flag_wheat);
		hmCurrImageMap.put("OIL", R.drawable.flag_oil);
		hmCurrImageMap.put("OIL2", R.drawable.flag_oil2);
		hmCurrImageMap.put("LLG", R.drawable.flag_gold);
		hmCurrImageMap.put("LLS", R.drawable.flag_silver);
		hmCurrImageMap.put("XPT", R.drawable.flag_platinum);
		hmCurrImageMap.put("CMHG", R.drawable.flag_copper);
		hmCurrImageMap.put("XPD", R.drawable.flag_palladium);
		hmCurrImageMap.put("THA", R.drawable.flag_tha);
		hmCurrImageMap.put("IDX", R.drawable.flag_idx);
		hmCurrImageMap.put("NOP", R.drawable.flag_no);
		hmCurrImageMap.put("HKS", R.drawable.flag_hkstock);
		hmCurrImageMap.put("HKS1", R.drawable.flag_hkstock1);
		hmCurrImageMap.put("HKS2", R.drawable.flag_hkstock2);
		hmCurrImageMap.put("00001", R.drawable.flag_0001);
		hmCurrImageMap.put("00005", R.drawable.flag_0005);
		hmCurrImageMap.put("00388", R.drawable.flag_0388);
		hmCurrImageMap.put("00700", R.drawable.flag_0700);
		hmCurrImageMap.put("00939", R.drawable.flag_0939);
		hmCurrImageMap.put("00941", R.drawable.flag_0941);
		hmCurrImageMap.put("01299", R.drawable.flag_1299);
		hmCurrImageMap.put("01398", R.drawable.flag_1398);
		hmCurrImageMap.put("02318", R.drawable.flag_2318);
		hmCurrImageMap.put("03988", R.drawable.flag_3988);
		hmCurrImageMap.put("GOLD", R.drawable.flag_gold);
		hmCurrImageMap.put("SILVER", R.drawable.flag_silver);
		hmCurrImageMap.put("USS1", R.drawable.flag_usstock1);
		hmCurrImageMap.put("USS2", R.drawable.flag_usstock2);
		hmCurrImageMap.put("GAS", R.drawable.flag_gas);

		hmCurrImageMap.put("CZK", R.drawable.flag_czk);
		hmCurrImageMap.put("DKK", R.drawable.flag_dkk);
		hmCurrImageMap.put("MXN", R.drawable.flag_mxn);
		hmCurrImageMap.put("NOK", R.drawable.flag_nok);
		hmCurrImageMap.put("RUB", R.drawable.flag_rub);
		hmCurrImageMap.put("SGD", R.drawable.flag_sgd);
		hmCurrImageMap.put("TRY", R.drawable.flag_try);
		hmCurrImageMap.put("SEK", R.drawable.flag_sek);
		hmCurrImageMap.put("ZAR", R.drawable.flag_zar);

		hmCurrImageMap.put("BTC", R.drawable.icon_btc);
	}

	/**
	 * System mesage count
	 */
	private int iMessageCount = 0;

	public void setMasters(Masters masters) {
		this.masters = masters;
		this.setHmMasters(masters.getMasterMap());
		this.setAlEmailMaster(masters.getGroupedAlMaster());
	}

	public void setAlEmailMaster(ArrayList<ArrayList<Master>> alEmailMaster) {
		this.alEmailMaster = alEmailMaster;
	}

	public Masters getMasters() {
		return masters;
	}

	public HashMap<Integer, Master> getHmMasters() {
		return hmMasters;
	}

	public void setHmMasters(HashMap<Integer, Master> hmMasters) {
		this.hmMasters = hmMasters;
	}

	public void setNewscontents(Newscontents newscontents) {
		this.newscontents = newscontents;
		this.setHmNewscontents(newscontents.getNewsContentMap());
	}

	public Newscontents getNewscontents() {
		return newscontents;
	}

	public void setHmNewscontents(HashMap<Integer, Newscontent> hmNewscontents) {
		this.hmNewscontents = hmNewscontents;
	}

	public HashMap<Integer, Newscontent> getHmNewscontents() {
		return hmNewscontents;
	}

	public Newses getNewses() {
		return newses;
	}

	public void setNewses(Newses newses) {
		this.newses = newses;
		this.setHmNewses(newses.getNewsMap());
	}


	public Strategies getStrategies() {
		return strategies;
	}

	public void setStrategies(Strategies strategies) {
		this.strategies = strategies;
		this.setHmStrategies(strategies.getStrategyMap());
		this.setHmStrategiesWithStr(strategies.getStrategyMapWithKeyStr(strategies.getStrategyList()));
		this.setAlDailyStrategy(strategies.getGroupedAlStrategy());
	}

	public Economicdatas getEconomicdatas() {
		return economicdatas;
	}

	public void setEconomicdatas(Economicdatas economicdatas) {
		this.economicdatas = economicdatas;
		this.setHmEconomicdatas(economicdatas.getEconomicdataMap());
	}

	public void setHourproducts(Hourproducts hourproducts) {
		this.hourproducts = hourproducts;
		this.setHmHourproducts(hourproducts.getHourProductMap());
	}

	public void setHmHourproducts(HashMap<Integer, Hourproduct> hmHourproducts) {
		this.hmHourproducts = hmHourproducts;
	}

	public void setHmEconomicdatas(HashMap<Integer, Economicdata> hmEconomicdatas) {
		this.hmEconomicdatas = hmEconomicdatas;
	}

	public void setAlDailyStrategy(ArrayList<ArrayList<Strategy>> alDailyStrategy) {
		this.alStrategy = alDailyStrategy;
	}

	public HashMap<Integer, News> getHmNewses() {
		return hmNewses;
	}

	public void setHmNewses(HashMap<Integer, News> hmNewses) {
		this.hmNewses = hmNewses;
	}

	public HashMap<Integer, Strategy> getHmStrategies() {
		return hmStrategies;
	}

	public void setHmStrategies(HashMap<Integer, Strategy> hmStrategies) {
		this.hmStrategies = hmStrategies;
	}

	public HashMap<String, Strategy> getHmStrategiesWithStr() {
		return hmStrategiesWithStr;
	}

	public void setHmStrategiesWithStr(HashMap<String, Strategy> hmStrategiesWithStr) {
		this.hmStrategiesWithStr = hmStrategiesWithStr;
	}

	public ArrayList<ArrayList<Strategy>> getAlStrategy() {
		return alStrategy;
	}

	public void setAlStrategy(ArrayList<ArrayList<Strategy>> alStrategy) {
		this.alStrategy = alStrategy;
	}

	public ContactUs getContactUs() {
		return contactUs;
	}

	public void setContactUs(ContactUs contactUs) {
		this.contactUs = contactUs;
	}

	public Other getOther() {
		return other;
	}

	public void setOther(Other other) {
		this.other = other;
	}

	public CompanyProfile getCompanyProfile() {
		return companyProfile;
	}

	public void setCompanyProfile(CompanyProfile companyProfile) {
		this.companyProfile = companyProfile;
	}

	public Advertisements getAdvertisements() {
		return advertisements;
	}

	public void setAdvertisements(Advertisements advertisements) {
		this.advertisements = advertisements;
	}

	public HashMap<String, Advertisement> getHmAdvertisements() {
		return hmAdvertisements;
	}

	public void setHmAdvertisements(HashMap<String, Advertisement> hmAdvertisements) {
		this.hmAdvertisements = hmAdvertisements;
	}

	/**
	 * Readable Contract
	 */
	public ArrayList<String> alTradableContract = new ArrayList<String>();

	/**
	 * List for storing master data
	 */
	private Masters masters = new Masters();
	/**
	 * HashMap for storing master data
	 */
	private HashMap<Integer, Master> hmMasters = new HashMap<Integer, Master>(20);

	/**
	 * ArrayList for storing master data by email
	 */
	private ArrayList<ArrayList<Master>> alEmailMaster = new ArrayList<ArrayList<Master>>(20);

	/**
	 * List for storing news content data
	 */
	private Newscontents newscontents = new Newscontents();
	/**
	 * HashMap for storing News content data
	 */
	private HashMap<Integer, Newscontent> hmNewscontents = new HashMap<Integer, Newscontent>(20);
	/**
	 * List for storing News data
	 */
	private Newses newses = new Newses();
	/**
	 * HashMap for storing News data
	 */
	private HashMap<Integer, News> hmNewses = new HashMap<Integer, News>(20);
	/**
	 * List for storing day plan data
	 */
	private Strategies strategies = new Strategies();
	/**
	 * HashMap for storing day plan data
	 */
	private HashMap<Integer, Strategy> hmStrategies = new HashMap<Integer, Strategy>(20);
	/**
	 * HashMap for storing day plan data which the key is String
	 */
	private HashMap<String, Strategy> hmStrategiesWithStr = new HashMap<String, Strategy>(20);
	/**
	 * ArrayList for storing Daily day data per date
	 */
	private ArrayList<ArrayList<Strategy>> alStrategy = new ArrayList<ArrayList<Strategy>>(20);
	/**
	 * List for storing Economic data
	 */
	private Economicdatas economicdatas = new Economicdatas();

	private Hourproducts hourproducts = new Hourproducts();
	private HashMap<Integer, Hourproduct> hmHourproducts = new HashMap<Integer, Hourproduct>(20);

	/**
	 * HashMap for storing Economic data
	 */
	private HashMap<Integer, Economicdata> hmEconomicdatas = new HashMap<Integer, Economicdata>(20);
	/**
	 * Store contact us data
	 */
	private ContactUs contactUs = new ContactUs();
	/**
	 * Store basic information, it is include privacy, risk and disclaimer
	 */
	private Other other = new Other();
	/**
	 * Store contact us data
	 */
	private CompanyProfile companyProfile = null;
	/**
	 * List for storing advertisement
	 */
	private Advertisements advertisements = new Advertisements();
	/**
	 * HashMap for storing advertisement
	 */
	private HashMap<String, Advertisement> hmAdvertisements = new HashMap<String, Advertisement>(20);
	/**
	 * HashMap for storing Price data
	 */
	//private HashMap<String, ContractObj> hmContractObj = new HashMap<String, ContractObj>(20);
	/**
	 * List for storing Price data
	 */
	//ArrayList<ContractObj> alContractObj = new ArrayList<ContractObj>();

	//private boolean isNewPriceMessage = false;
	private ConnectionStatus guestPriceAgentConnectionStatus = ConnectionStatus.INITIAL;
	private int guestPriceAgentConnectionId = -1;

	/**
	 * Constructor
	 */
	private DataRepository(){

	}

	/**
	 * Get DataRepository instance
	 * @return instance of DataRepository
	 */
	public static DataRepository getInstance(){
		return instance;
	}

	/**
	 * Get user code
	 * @return user code
	 */
	public String getStrUser() {
		return strUser;
	}

	/**
	 * Set user code
	 * @param strUser user code
	 */
	public void setStrUser(String strUser) {
		this.strUser = strUser;
	}

	/**
	 * Get user password
	 * @return user password
	 */
	public String getStrPassword() {
		return strPassword;
	}

	/**
	 * Set user password
	 * @param strPassword user password
	 */
	public void setStrPassword(String strPassword) {
		this.strPassword = strPassword;
	}

	public String getStrEmail() {
		return strEmail;
	}

	public void setStrEmail(String strEmail) {
		this.strEmail = strEmail;
	}

	/**
	 * Get server URL
	 * @return server URL
	 */
	public String getStrURL() {
		return strURL;
	}

	/**
	 * Set server URL
	 * @param strURL server URL
	 */
	public void setStrURL(String strURL) {
		this.strURL = strURL;
	}

	/**
	 * Get connection port
	 * @return port number
	 */
	public int getiPort() {
		return iPort;
	}
	/**
	 * Set connection port number
	 * @param iPort
	 */
	public void setiPort(int iPort) {
		this.iPort = iPort;
	}

	private ArrayList<String> alChartAva = new ArrayList<String>();

	public void addChartAva(String sContract){
		alChartAva.add(sContract);
	}

	public boolean isChartAva(String sContract){
		return alChartAva.contains(sContract);
	}

	public void clearAllChartAva(){
		alChartAva.clear();
	}

	private String pushyToken = null;
	private boolean priceAlertUpdateEnable = false;
	/**
	 * Add contract object
	 * @param contract contract object
	 */
	public void addContract(ContractObj contract){
		//System.out.println("Add contract" + contract);

		if(!hmContract.containsKey(contract.strContractCode)){
			hmContract.put(contract.strContractCode, contract);
			alContract.add(contract);

			if(!alContractSeq.contains(contract.strContractCode)){
				alContractSeq.add(contract.strContractCode);
			}

			if(!alContractDefaultSeq.contains(contract.strContractCode)){
				alContractDefaultSeq.add(contract.strContractCode);
			}

			hmContractName.put(contract.sENName, contract);
			hmContractName.put(contract.sTCName, contract);
			hmContractName.put(contract.sSCName, contract);
			hmContractName.put(contract.strContractCode, contract);

			//String[] sCurr = Utility.getCurrency(contract.strContractCode);
			/*
			if(sCurr != null){
				if(sCurr[0].equals("RKG")&&sCurr[1].equals("HKD")){
					contract.iCurr1Image = this.getContractImage(sCurr[0]);
					contract.iCurr2Image = this.getContractImage("RMB");
				}else{
					contract.iCurr1Image = this.getContractImage(sCurr[0]);
					contract.iCurr2Image = this.getContractImage(sCurr[1]);
				}
			} else
			*/
			{
				if(isContractImageExist(contract.strContractCode)){
					int[] images=this.getContractImage(contract.strContractCode);
					contract.iCurr1Image = images[0];
					contract.iCurr2Image = images[1];
				}else{
					contract.iCurr1Image = hmCurrImageMap.get("NOP");
					contract.iCurr2Image = hmCurrImageMap.get("NOP");
				}
			}

		}
	}

	/**
	 * Get contract object by contract name
	 * @param sName contract name
	 * @return contract object
	 */
	public ContractObj getContractByName(String sName){
		return hmContractName.get(sName);
	}

	/**
	 * Get contract object by contract code
	 * @param sItem contract code
	 * @return contract object
	 */
	public ContractObj getContract(String sItem){
		return hmContract.get(sItem);
	}

	/**
	 * Is contract exist
	 * @param sItem contract code
	 * @return
	 */
	public boolean isContractExist(String sItem){
		return hmContract.containsKey(sItem);
	}
	/**
	 * Remove contract by contract code
	 * @param sItem contract code
	 * @return target contract, if contract didn't existing, it will return null;
	 */
	public ContractObj removeContract(String sItem){
		if(hmContract.containsKey(sItem)){
			ContractObj contract = hmContract.remove(sItem);
			alContract.remove(contract);
			hmContractName.remove(contract.sENName);
			hmContractName.remove(contract.sTCName);
			hmContractName.remove(contract.sSCName);
			hmContractName.remove(contract.strContractCode);
			return contract;
		}else{
			return null;
		}
	}

	/**
	 * Get contract map
	 * @return HashMap for contract
	 */
	public HashMap<String, ContractObj> getContracts(){
		return hmContract;
	}

	/**
	 * Remove contract by contract object
	 * @param contract contract object
	 * @return target contract, if contract didn't existing, it will return null
	 */
	public ContractObj removeContractObj(ContractObj contract){
		return removeContract(contract.strContractCode);
	}

	/**
	 * Get open position map
	 * @return HashMap for open position
	 */
	public HashMap<Integer, OpenPositionRecord> getOpenPositions(){
		return hmPosition;
	}

	/**
	 * Remove open position by open position object
	 * @param position target open position, if open position didn't existing, it will return null
	 * @return
	 */
	public OpenPositionRecord removeOpenPosition(OpenPositionRecord position){
		return removeOpenPosition(position.iRef);
	}

	/**
	 * Add open position
	 * @param position open position object
	 */
	public void addOpenPosition(OpenPositionRecord position){
		if(!hmPosition.containsKey(position.iRef)){
			hmPosition.put(position.iRef, position);
			alPosition.add(position);
		}
	}

	/**
	 * Get open position by reference number
	 * @param iRef open position reference number
	 * @return target open position record
	 */
	public OpenPositionRecord getOpenPosition(int iRef){
		return hmPosition.get(iRef);
	}
	/**
	 * Check is open position exist
	 * @param iRef reference number
	 * @return is open position exist
	 */
	public boolean isOpenPositionExist(Integer iRef){
		return hmPosition.containsKey(iRef);
	}
	/**
	 * Remove open position by reference number
	 * @param iRef open position reference number
	 * @return open position
	 */
	public OpenPositionRecord removeOpenPosition(int iRef){
		if(hmPosition.containsKey(iRef)){
			OpenPositionRecord position = hmPosition.remove(iRef);
			alPosition.remove(position);
			return position;
		}else{
			return null;
		}
	}
	/**
	 * Get transaction map
	 * @return transaction map
	 */
	public HashMap<String, TransactionObj> getTransactions(){
		return hmTransaction;
	}
	/**
	 * Remove transaction by transaction object
	 * @param Transaction target transaction object
	 * @return target transaction object
	 */

	public TransactionObj removeTransaction(TransactionObj Transaction){
		return removeTransaction(Transaction.sTransactionID);
	}

	/**
	 * Add Transaction object
	 * @param Transaction transaction object
	 */

	public void addTransaction(TransactionObj Transaction){
		if(!hmTransaction.containsKey(Transaction.sTransactionID)){
			hmTransaction.put(Transaction.sTransactionID, Transaction);
			alTransaction.add(Transaction);
		}
	}

	public TransactionObj updateTransaction(TransactionObj t) {
		TransactionObj replace = hmTransaction.replace(t.sTransactionID, t);
		if (replace != null) {
			alTransaction.replaceAll((temp) -> {
				if (Objects.equals(temp.sTransactionID, t.sTransactionID)) {
					return t;
				}
				return temp;
			});
		}
		return replace;
	}

	/**
	 * Get transaction object by ID
	 * @param sID transaction id
	 * @return Target transaction object
	 */
	public TransactionObj getTransaction(String sID){
		return hmTransaction.get(sID);
	}
	/**
	 * Check is transaction object exist by transaction ID
	 * @param sID transaction ID
	 * @return is transaction exist?
	 */
	public boolean isTransactionExist(String sID){
		return hmTransaction.containsKey(sID);
	}
	/**
	 * Remove target transaction order by transaction ID
	 * @param sID transaction ID
	 * @return Target transaction object
	 */
	public TransactionObj removeTransaction(String sID){
		if(hmTransaction.containsKey(sID)){
			TransactionObj Transaction = hmTransaction.remove(sID);
			alTransaction.remove(Transaction);
			return Transaction;
		}else{
			return null;
		}
	}

	/**
	 * Get running order map
	 * @return map for running order
	 */
	public Map<Integer, OrderRecord> getRunningOrders(){
		return hmRunningOrder;
	}
	/**
	 * Remove running order by order object
	 * @param order target order
	 * @return target order
	 */
	public OrderRecord removeRunningOrder(OrderRecord order){
		return removeRunningOrder(order.iRef);
	}
	/**
	 * Add running order
	 * @param order
	 */
	public void addRunningOrder(OrderRecord order){
		if(!hmRunningOrder.containsKey(order.iRef)){
			HashMap<Integer, OrderRecord> m = new HashMap<>(hmRunningOrder);
			m.put(order.iRef, order);
			hmRunningOrder = Collections.unmodifiableMap(m);
			alRunningOrder.add(order);
		}
	}
	/**
	 * Get running order by ID
	 * @param iID order ID
	 * @return Target order object
	 */
	public OrderRecord getRunningOrder(int iID){
		return hmRunningOrder.get(iID);
	}
	/**
	 * Check is running order exist?
	 * @param iID target order ID
	 * @return exist or not?
	 */
	public boolean isRunningOrderExist(int iID){
		return hmRunningOrder.containsKey(iID);
	}
	/**
	 * Remove running order by Order ID
	 * @param iID target order ID
	 * @return target order object
	 */
	public OrderRecord removeRunningOrder(int iID){
		if(hmRunningOrder.containsKey(iID)){
			HashMap<Integer, OrderRecord> m = new HashMap<>(hmRunningOrder);
			OrderRecord RunningOrder = m.remove(iID);
			hmRunningOrder = Collections.unmodifiableMap(m);
			alRunningOrder.remove(RunningOrder);
			return RunningOrder;
		}else{
			return null;
		}
	}
	/**
	 * Get executed order map
	 * @return map for executed order
	 */
	public HashMap<Integer, ExecutedOrder> getExecutedOrders(){
		return hmExecutedOrder;
	}
	/**
	 * Remove executed order by executed order object
	 * @param order target executed order
	 * @return target executed order
	 */
	public OrderRecord removeExecutedOrder(ExecutedOrder order){
		return removeExecutedOrder(order.iRef);
	}
	/**
	 * Add executed order
	 * @param order
	 */
	public void addExecutedOrder(ExecutedOrder order){
		if(!hmExecutedOrder.containsKey(order.iRef)){
			hmExecutedOrder.put(order.iRef, order);
			alExecutedOrder.add(order);
		}
	}
	/**
	 * Get executed order by ID
	 * @param iID target executed order ID
	 * @return target executed order
	 */
	public ExecutedOrder getExecutedOrder(int iID){
		return hmExecutedOrder.get(iID);
	}
	/**
	 * Check is executed order exist or not?
	 * @param iID
	 * @return exist or not
	 */
	public boolean isExecutedOrderExist(int iID){
		return hmExecutedOrder.containsKey(iID);
	}
	/**
	 * Remove executed order by ID
	 * @param iID target executed order ID
	 * @return target executed order object
	 */
	public ExecutedOrder removeExecutedOrder(int iID){
		if(hmExecutedOrder.containsKey(iID)){
			ExecutedOrder executedOrder = hmExecutedOrder.remove(iID);
			alExecutedOrder.remove(executedOrder);
			return executedOrder;
		}else{
			return null;
		}
	}
	/**
	 * Clear executed order
	 */
	public void clearExecutedOrder(){
		hmExecutedOrder.clear();
		alExecutedOrder.clear();
	}
	/**
	 * Get canceled order map
	 * @return map for canceled order
	 */
	public HashMap<Integer, CancelledOrder> getCancelledOrders(){
		return hmCancelledOrder;
	}
	/**
	 * Remove canceled order by object
	 * @param order target canceled order
	 * @return target canceled order
	 */
	public OrderRecord removeCancelledOrder(CancelledOrder order){
		return removeCancelledOrder(order.iRef);
	}
	/**
	 * Add canceled order
	 * @param order canceled order
	 */
	public void addCancelledOrder(CancelledOrder order){
		if(!hmCancelledOrder.containsKey(order.iRef)){
			hmCancelledOrder.put(order.iRef, order);
			alCancelledOrder.add(order);
		}
	}
	/**
	 * Get canceled order by ID
	 * @param iID canceled order ID
	 * @return target canceled order object
	 */
	public CancelledOrder getCancelledOrder(int iID){
		return hmCancelledOrder.get(iID);
	}
	/**
	 * Check is canceled order exist or not?
	 * @param iID target canceled order ID
	 * @return Is record exist or not
	 */
	public boolean isCancelledOrderExist(int iID){
		return hmCancelledOrder.containsKey(iID);
	}
	/**
	 * Remove canceled order by ID
	 * @param iID target canceled order ID
	 * @return target canceled order
	 */
	public CancelledOrder removeCancelledOrder(int iID){
		if(hmCancelledOrder.containsKey(iID)){
			CancelledOrder cancelledOrder = hmCancelledOrder.remove(iID);
			alCancelledOrder.remove(cancelledOrder);
			return cancelledOrder;
		}else{
			return null;
		}
	}
	/**
	 * Clear canceled order
	 */
	public void clearCancelledOrder(){
		hmCancelledOrder.clear();
		alCancelledOrder.clear();
	}
	/**
	 * Get liquidation history map
	 * @return liquidation history map
	 */
	public HashMap<String, LiquidationRecord> getLiquidationRecords(){
		return hmLiquidationRecord;
	}
	/**
	 * Remove liquidation history by object
	 * @param record target liquidation history object
	 * @return target liquidation history object
	 */
	public LiquidationRecord removeLiquidationRecord(LiquidationRecord record){
		return removeLiquidationRecord(record.getKey());
	}
	/**
	 * Add liquidation history
	 * @param record liquidation history
	 */
	public void addLiquidationRecord(LiquidationRecord record){
		if(!hmLiquidationRecord.containsKey(record.getKey())){
			hmLiquidationRecord.put(record.getKey(), record);
			alLiquidationRecord.add(record);
		}
	}
	/**
	 * Get liquidation history buy key
	 * @param sKey Key
	 * @return target liquidation history object
	 */
	public LiquidationRecord getLiquidationRecord(String sKey){
		return hmLiquidationRecord.get(sKey);
	}
	/**
	 * Check is liquidation history exist or not
	 * @param sKey Key
	 * @return Exist or not
	 */
	public boolean isLiquidationRecordExist(String sKey){
		return hmLiquidationRecord.containsKey(sKey);
	}
	/**
	 * Remove liquidation history by Key
	 * @param sKey Key
	 * @return target liquidation history object
	 */
	public LiquidationRecord removeLiquidationRecord(String sKey){
		if(hmLiquidationRecord.containsKey(sKey)){
			LiquidationRecord cancelledOrder = hmLiquidationRecord.remove(sKey);
			alLiquidationRecord.remove(cancelledOrder);
			return cancelledOrder;
		}else{
			return null;
		}
	}
	/**
	 * Clear liquidation history
	 */
	public void clearLiquidationRecord(){
		hmLiquidationRecord.clear();
		alLiquidationRecord.clear();
	}

	/**
	 * Get CashMovement history map
	 * @return CashMovement history map
	 */
	public Map<String, CashMovementRecord> getCashMovementRecords(){
		return hmCashMovementRecord;
	}
	/**
	 * Remove CashMovement history by object
	 * @param record target CashMovement history object
	 * @return target CashMovement history object
	 */
	public CashMovementRecord removeCashMovementRecord(CashMovementRecord record){
		return removeCashMovementRecord(record.getKey());
	}
	/**
	 * Add CashMovement history
	 * @param record CashMovement history
	 */
	public void addCashMovementRecord(CashMovementRecord record){
		if(!hmCashMovementRecord.containsKey(record.getKey())){
			Map<String, CashMovementRecord> map = new HashMap<>(hmCashMovementRecord);
			map.put(record.getKey(), record);
			hmCashMovementRecord = Collections.unmodifiableMap(map);
			alCashMovementRecord.add(record);
		}
	}
	/**
	 * Get CashMovement history buy key
	 * @param sKey Key
	 * @return target CashMovement history object
	 */
	public CashMovementRecord getCashMovementRecord(String sKey){
		return hmCashMovementRecord.get(sKey);
	}
	/**
	 * Check is CashMovement history exist or not
	 * @param sKey Key
	 * @return Exist or not
	 */
	public boolean isCashMovementRecordExist(String sKey){
		return hmCashMovementRecord.containsKey(sKey);
	}
	/**
	 * Remove CashMovement history by Key
	 * @param sKey Key
	 * @return target CashMovement history object
	 */
	public CashMovementRecord removeCashMovementRecord(String sKey){
		if(hmCashMovementRecord.containsKey(sKey)){
			Map<String, CashMovementRecord> map = new HashMap<>(hmCashMovementRecord);

			CashMovementRecord cancelledOrder = map.remove(sKey);
			hmCashMovementRecord = Collections.unmodifiableMap(map);
			alCashMovementRecord.remove(cancelledOrder);
			return cancelledOrder;
		}else{
			return null;
		}
	}
	/**
	 * Clear CashMovement history
	 */
	public void clearCashMovementRecord(){
		hmCashMovementRecord = Collections.unmodifiableMap(Collections.EMPTY_MAP);
		alCashMovementRecord.clear();
		if(CashMovementRecord.userBankName!=null)
			CashMovementRecord.userBankName=null;
		if(CashMovementRecord.userAccountNumber!=null)
			CashMovementRecord.userAccountNumber=null;
	}

	/**
	 * Get system message map
	 * @return system message amp
	 */
	public Map<Integer, SystemMessage> getSystemMessages(){
		return hmSysMsg;
	}
	/**
	 * Remove system message by object
	 * @param sysMsg target system message
	 * @return target system message
	 */
	public SystemMessage removeSystemMessage(SystemMessage sysMsg){
		return removeSystemMessage(sysMsg.iID);
	}
	/**
	 * Add system message
	 * @param sysMsg system message object
	 */
	public void addSystemMessage(SystemMessage sysMsg){
		if(!hmSysMsg.containsKey(sysMsg.iID)){
			iMessageCount++;
			HashMap<Integer, SystemMessage> m = new HashMap<>(hmSysMsg);
			m.put(sysMsg.iID, sysMsg);
			hmSysMsg = Collections.unmodifiableMap(m);
			alSysMsg.add(sysMsg);
		}
	}
	/**
	 * Get count of system message
	 * @return number of system message
	 */
	public int getSystemMessageCount(){
		return iMessageCount;
	}
	/**
	 * Reset system message count
	 */
	public void resetSystemMessageCount(){
		iMessageCount = 0;
	}
	/**
	 * Get system message by ID
	 * @param sID message ID
	 * @return target system message
	 */
	public SystemMessage getSystemMessage(Integer sID){
		return hmSysMsg.get(sID);
	}
	/**
	 * Check is system message exist or not
	 * @param sID message ID
	 * @return target system message
	 */
	public boolean isSystemMessageExist(Integer sID){
		return hmSysMsg.containsKey(sID);
	}
	/**
	 * Remove system message by ID
	 * @param sID target ID
	 * @return target system message
	 */
	public SystemMessage removeSystemMessage(Integer sID){
		if(hmSysMsg.containsKey(sID)){
			HashMap<Integer, SystemMessage> m = new HashMap<>(hmSysMsg);
			SystemMessage sysMsg = m.remove(sID);
			hmSysMsg = Collections.unmodifiableMap(m);
			alSysMsg.remove(sysMsg);
			return sysMsg;
		}else{
			return null;
		}
	}
	/**
	 * Get balance record
	 * @return balance record
	 */
	public BalanceRecord getBalanceRecord() {
		return balance;
	}
	/**
	 * Set balance record
	 * @param balance balance record
	 */
	public void setBalanceRecord(BalanceRecord balance) {
		this.balance = balance;
	}
	/**
	 * Get contract list
	 * @return
	 */
	public ArrayList<ContractObj> getContractList(){
		return alContract;
	}
	public ArrayList<ContractObj> getTradableContractList()
	{
		ArrayList<ContractObj> contractList = new ArrayList<ContractObj>();
		synchronized(alContract){
			for(ContractObj contract : alContract){
				if(alTradableContract.contains(contract.strContractCode) && contract.isViewable()){
					contractList.add(contract);
				}
			}

		}
		return contractList;
	}

	/**
	 * Clear all record
	 */
	public void clear() {
		if(balance != null){
			balance.destory();
			balance = null;
		}

		hmContract.clear();
		for(ContractObj contract : alContract){
			contract.destory();
		}

		alContract.clear();

		hmPosition.clear();
		for(OpenPositionRecord position : alPosition){
			position.destory();
		}

		hmRunningOrder = Collections.EMPTY_MAP;
		for(OrderRecord order : alRunningOrder){
			order.destory();
		}

		hmExecutedOrder.clear();
		for(ExecutedOrder order : alExecutedOrder){
			order.destory();
		}

		hmCancelledOrder.clear();
		for(CancelledOrder order : alCancelledOrder){
			order.destory();
		}

		hmLiquidationRecord.clear();
		for(LiquidationRecord record : alLiquidationRecord){
			record.destory();
		}

		hmTransaction.clear();
		for(TransactionObj record : alTransaction){
			record.destory();
		}

		clearCashMovementRecord();
		clearSystemMessage();

		PriceMessageObj.cleanUp();

		//isNewPriceMessage = false;

		depositTermsMap = Collections.EMPTY_MAP;
		withdrawalTermsMap = Collections.EMPTY_MAP;
	}

	public void clearSystemMessage(){
		hmSysMsg = Collections.EMPTY_MAP;
		alSysMsg.clear();
		resetSystemMessageCount();
	}
	/**
	 * Add contract image mapping
	 * @
	 * param sContract contract code
	 * @param iImage image ID
	 */
	public void addContractImage(String sContract, String curr1, String curr2){
		int image1=0;
		if(hmCurrImageMap.containsKey(curr1))
			image1=hmCurrImageMap.get(curr1);
		else
			image1=hmCurrImageMap.get("NOP");

		int image2=0;
		if(hmCurrImageMap.containsKey(curr2))
			image2=hmCurrImageMap.get(curr2);
		else
			image2=hmCurrImageMap.get("NOP");

		hmContractImage.put(sContract, new int[]{image1,image2});
	}
	/**
	 * Get contract image ID
	 * @param sContract target contract
	 * @return image ID
	 */
	public int[] getContractImage(String sContract){
		return hmContractImage.get(sContract);
	}
	/**
	 * Check is contract image exist or not
	 * @param sContract Contract Code
	 * @return exist or not
	 */
	public boolean isContractImageExist(String sContract){
		return hmContractImage.containsKey(sContract);
	}

	public void clearAllContractImage(){
		hmContractImage.clear();
	}

	public void setContractSequence(String sSeq){
		String[] seqs = sSeq.split("\\|");
		synchronized(alContract){
			alContractSeq.clear();
			for(String sContract : seqs){
				if(!sContract.equals("")){
					alContractSeq.add(sContract);
				}
			}

		}
		seqs = null;
	}
    public void setContractSequenceSort(List<String> contractSeqSort) {
	    this.alContractSeqSort = Collections.unmodifiableList(contractSeqSort);
    }

	public ArrayList<String> getContractSequence(){
		return alContractSeq;
	}

	public List<String> getContractSequenceSort() {
	    return alContractSeqSort;
    }

	public List<String> getContractSequenceSortDefault() {
	    return alContractDefaultSeq;
    }

	public ArrayList<String>  getContractSequenceSortDefault(boolean isLogin){
		//only display tradable contract when account is logined, otherwise, display all contract(guest)
		synchronized(alContractDefaultSeq) {
			ArrayList<String> alContractDefaultSeqTemp = (ArrayList<String>) alContractDefaultSeq.clone();
			for (String s : alContractDefaultSeqTemp) {
				ContractObj c = hmContract.get(s);
				if (c != null) {
					if (isLogin) {
						if (!(c.isViewable() && (alTradableContract.contains(c.strContractCode) || CompanySettings.SHOW_NON_TRADEABLE_ITEM == true)))
							alContractDefaultSeq.remove(c.strContractCode);
					}
				}
			}
		}
		return alContractDefaultSeq;
	}

	public void setTradableContract(String sTradableContract){
		if (sTradableContract != null && sTradableContract.length() > 0){
			if (sTradableContract.equals("ALL")){
				alTradableContract.clear();
			}
			else{
				alTradableContract.clear();

				StringTokenizer st = new StringTokenizer(sTradableContract, "|");
				while(st.hasMoreElements()){
					String tmpContract = (String)st.nextToken();
					if (tmpContract != null && tmpContract.length() > 0)
						alTradableContract.add(tmpContract);
					tmpContract = null;
				}
				st = null;
			}
		}
		else
		{
			alTradableContract.clear();
		}
	}

	public ArrayList<String> getTradableContract(){
		return alTradableContract;
	}

	public void cleanContract() {
		synchronized(hmContract){
			hmContract.clear();
			alContract.clear();
		}
	}


	public HashMap<Integer, Economicdata> getHmEconomicdatas() {
		return hmEconomicdatas;
	}

	public ArrayList<ArrayList<Master>> getAlEmailMaster() {
		return alEmailMaster;
	}

	public ArrayList<ArrayList<Strategy>> getAlDailyStrategy() {
		return alStrategy;
	}

	public HashMap<Integer, Hourproduct> getHmHourproducts() {
		return getHourproducts().getHourProductMap();
	}

	public Hourproducts getHourproducts() {
		return hourproducts;
	}

	/*
	public boolean IsNewPriceMessage()
	{
		return isNewPriceMessage;
	}

	public void setNewPriceMessage(boolean isNewPriceMessage)
	{
		this.isNewPriceMessage = isNewPriceMessage;
	}*/

	/**
	 * HashMap for storing a mapping for 2 FA information
	 */
	private HashMap<String, String> hmTwoFA = new HashMap<String, String>();

	public HashMap<String, String> getTwoFAMessage() {
		return hmTwoFA;
	}

	public boolean twoFARefreshTimer = true;

	public void setTwoFAMessage(Message loginMsg){
		hmTwoFA.put(ServiceFunction.LOGIN_ID, loginMsg.getData().getString(ServiceFunction.LOGIN_ID));
		hmTwoFA.put(ServiceFunction.LOGIN_PASSWORD, loginMsg.getData().getString(ServiceFunction.LOGIN_PASSWORD));
		hmTwoFA.put(ServiceFunction.LOGIN_PLATFORM_TYPE, loginMsg.getData().getString(ServiceFunction.LOGIN_PLATFORM_TYPE));
		hmTwoFA.put(ServiceFunction.LOGIN_CONN_INDEX, Integer.toString(loginMsg.getData().getInt(ServiceFunction.LOGIN_CONN_INDEX)));
	}

	public void setTwoFAMessage(MessageObj msgObj){
		HashMap<String, String> hmTwoFATemp = new HashMap<String, String>();
		hmTwoFATemp = hmTwoFA;
		hmTwoFA = new HashMap<String, String>();

		hmTwoFA.put(ServiceFunction.LOGIN_ID, hmTwoFATemp.get(ServiceFunction.LOGIN_ID));
		hmTwoFA.put(ServiceFunction.LOGIN_PASSWORD, hmTwoFATemp.get(ServiceFunction.LOGIN_PASSWORD));
		hmTwoFA.put(ServiceFunction.LOGIN_PLATFORM_TYPE, hmTwoFATemp.get(ServiceFunction.LOGIN_PLATFORM_TYPE));
		hmTwoFA.put(ServiceFunction.LOGIN_CONN_INDEX, hmTwoFATemp.get(ServiceFunction.LOGIN_CONN_INDEX));

//		hmTwoFA.put(Protocol.LoginResponse.TWO_FA, msgObj.getField(Protocol.LoginResponse.TWO_FA));
//		hmTwoFA.put(Protocol.LoginResponse.TWO_FA_PREFIX, msgObj.getField(Protocol.LoginResponse.TWO_FA_PREFIX));
//		hmTwoFA.put(Protocol.LoginResponse.TWO_FA_EXPIRY, msgObj.getField(Protocol.LoginResponse.TWO_FA_EXPIRY));
//		hmTwoFA.put(Protocol.LoginResponse.TWO_FA_MOBILE, msgObj.getField(Protocol.LoginResponse.TWO_FA_MOBILE));
//		hmTwoFA.put(Protocol.LoginResponse.TWO_FA_EMAIL, msgObj.getField(Protocol.LoginResponse.TWO_FA_EMAIL));
	}

	public int sessTimeoutAlert = -1;
	public int sessTimeoutDisconn = -1;

	public boolean timeoutAlert = false;

	private String systemAlertMessage = "";
	public void setSystemAlertMessage(String msg){
		systemAlertMessage = msg;
	}
	public String getSystemAlertMessage(){
		return systemAlertMessage;
	}

	public ConnectionStatus getGuestPriceAgentConnectionStatus() {
		return guestPriceAgentConnectionStatus;
	}

	public void setGuestPriceAgentConnectionStatus(ConnectionStatus guestPriceAgentConnectionStatus) {
		this.guestPriceAgentConnectionStatus = guestPriceAgentConnectionStatus;
	}

	public int getGuestPriceAgentConnectionId() {
		return guestPriceAgentConnectionId;
	}

	public void setGuestPriceAgentConnectionId(int guestPriceAgentConnectionId) {
		this.guestPriceAgentConnectionId = guestPriceAgentConnectionId;
	}

	public void setPushyToken(String token){
		this.pushyToken = token;
	}

	public String getPushyToken(){
		return pushyToken;
	}
}

