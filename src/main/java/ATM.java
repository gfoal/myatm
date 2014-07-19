public class ATM {
    private double moneyInATM;
    private Card card;
    private boolean valid = false;
    private int pinCode;

    //Можно задавать количество денег в банкомате 
    ATM(double moneyInATM){
        this.moneyInATM = moneyInATM;
    }

    public double getMoneyInATM() {
        return moneyInATM;
    }

    //С вызова данного метода начинается работа с картой
    //Метод принимает карту и пин-код, проверяет пин-код карты и не заблокирована ли она
    //Если неправильный пин-код или карточка заблокирована, возвращаем false. При этом, вызов всех последующих методов у ATM с данной картой должен генерировать исключение NoCardInserted
    public boolean validateCard(Card card, int pinCode){
        this.card = card;
        this.pinCode = pinCode;

        if (card.checkPin(pinCode) && !card.isBlocked()) {
            return true;
        } else {
            return false;
        }
    }

    //Возвращает сколько денег есть на счету
    public double checkBalance() throws NoCardInserted {
        valid = validateCard(card, pinCode);

        if (!valid) {
            throw new NoCardInserted();
        } else {
            return card.getAccount().getBalance();
        }
    }

    //Метод для снятия указанной суммы
    //Метод возвращает сумму, которая у клиента осталась на счету после снятия
    //Кроме проверки счета, метод так же должен проверять достаточно ли денег в самом банкомате
    //Если недостаточно денег на счете, то должно генерироваться исключение NotEnoughMoneyInAccount 
    //Если недостаточно денег в банкомате, то должно генерироваться исключение NotEnoughMoneyInATM 
    //При успешном снятии денег, указанная сумма должна списываться со счета, и в банкомате должно уменьшаться количество денег
    public double getCash(double amount) throws RuntimeException {
        valid = validateCard(card, pinCode);

        if (!valid) {
            throw new NoCardInserted();
        } else if (card.getAccount().getBalance() < amount) {
            throw new NotEnoughMoneyInAccount();
        } else if (getMoneyInATM() < amount) {
            throw new NotEnoughMoneyInATM();
        } else {
            card.getAccount().withdraw(amount);
            moneyInATM -= amount;
            return card.getAccount().getBalance();
        }
    }
}
