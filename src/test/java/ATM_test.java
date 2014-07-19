import org.junit.*;
import org.mockito.InOrder;

import static org.mockito.Mockito.*;

public class ATM_test {
    // These are unit tests for ATM class
    // Test are named using such name convention: [MethodName_StateUnderTest_ExpectedBehavior]

    @Test
    public void validateCard_pinIsIncorrect_returnsFalse() {
        ATM atm = new ATM(100);
        int pin = 1234;

        Card card = mock(Card.class);
        when(card.checkPin(anyInt())).thenReturn(false);
        when(card.isBlocked()).thenReturn(false);

        Assert.assertFalse("Card is validated despite pin is incorrect", atm.validateCard(card, pin));
    }

    @Test
    public void validateCard_cardIsBlocked_returnsFalse() {
        ATM atm = new ATM(100);
        int pin = 1234;
        Card card = mock(Card.class);
        when(card.checkPin(anyInt())).thenReturn(true);
        when(card.isBlocked()).thenReturn(true);

        Assert.assertFalse("Card is validated despite it is blocked", atm.validateCard(card, pin));
    }

    @Test
    public void validateCard_pinIsCorrectAndCardIsNotBlocked_returnsTrue() {
        ATM atm = new ATM(100);
        int pin = 1234;

        Card card = mock(Card.class);
        when(card.checkPin(anyInt())).thenReturn(true);
        when(card.isBlocked()).thenReturn(false);

        Assert.assertTrue("Card is not validated although pin is correct and card is not blocked",
                atm.validateCard(card, pin));
    }

    @Test(expected = NoCardInserted.class)
    public void checkBalance_cardIsNotValid_exceptionIsThrown() {
        ATM atm = new ATM(100);

        Card card = mock(Card.class);
        when(atm.validateCard(card, anyInt())).thenReturn(false);

        atm.checkBalance();
    }

    @Test
    public void checkBalance_cardIsValid_returnsBalance() {
        ATM atm = new ATM(100);
        double expected = 12;

        Card card = mock(Card.class);
        Account account = mock(Account.class);
        when(atm.validateCard(card, anyInt())).thenReturn(true);
        when(card.getAccount()).thenReturn(account);
        when(atm.checkBalance()).thenReturn(12.0);

        Assert.assertEquals("Balance is returned incorrectly", expected, atm.checkBalance(), 0.01);
    }

    @Test(expected = NoCardInserted.class)
    public void getCash_cardIsNotValid_exceptionIsThrown() {
        ATM atm = new ATM(100);

        Card card = mock(Card.class);
        when(atm.validateCard(card, anyInt())).thenReturn(false);

        atm.getCash(50);
    }

    @Test(expected = NotEnoughMoneyInAccount.class)
    public void getCash_notEnoughMoneyInAccount_exceptionIsThrown() {
        ATM atm = new ATM(100);

        Card card = mock(Card.class);
        Account account = mock(Account.class);
        when(atm.validateCard(card, anyInt())).thenReturn(true);
        when(card.getAccount()).thenReturn(account);
        when(account.getBalance()).thenReturn(40.0);

        atm.getCash(50);
    }

    @Test(expected = NotEnoughMoneyInATM.class)
     public void getCash_notEnoughMoneyInATM_exceptionIsThrown() {
        ATM atm = new ATM(100);

        Card card = mock(Card.class);
        Account account = mock(Account.class);
        when(atm.validateCard(card, anyInt())).thenReturn(true);
        when(card.getAccount()).thenReturn(account);
        when(account.getBalance()).thenReturn(300.0);

        atm.getCash(120);
    }

    @Test
    public void getCash_validCardAndEnoughMoneyInATMAndEnoughMoneyInAccount_moneyInATMIsWithdrawn() {
        double expected = 45.0;
        ATM atm = new ATM(100);

        Card card = mock(Card.class);
        Account account = mock(Account.class);
        when(atm.validateCard(card, anyInt())).thenReturn(true);
        when(card.getAccount()).thenReturn(account);
        when(account.getBalance()).thenReturn(300.0);

        atm.getCash(55);

        Assert.assertEquals("Money is not withdrawn from ATM", expected, atm.getMoneyInATM(), 0.01);
    }

    @Test
    public void getCash_validCardAndEnoughMoneyInATMAndEnoughMoneyInAccount_methodsAreCalledInCorrectOrder() {
        ATM atm = new ATM(100);

        Card card = mock(Card.class);
        Account account = mock(Account.class);
        when(atm.validateCard(card, anyInt())).thenReturn(true);
        when(card.getAccount()).thenReturn(account);
        when(account.getBalance()).thenReturn(300.0);

        atm.getCash(55);

        InOrder io = inOrder(card, account);
        io.verify(card, atLeastOnce()).checkPin(anyInt());
        io.verify(card, atLeastOnce()).isBlocked();
        io.verify(account, times(1)).withdraw(anyDouble());
    }
}
