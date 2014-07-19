public class MyATM {

    public static void main(String[] args) throws Throwable {
        double moneyInATM = 1000;
        ATM atm = new ATM(moneyInATM);
        Card card = null;
        atm.validateCard(card, 1234); // NullPointerException
        atm.checkBalance();
        atm.getCash(999.99);        
    }
}
