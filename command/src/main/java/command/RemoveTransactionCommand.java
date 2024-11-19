package command;

import model.Account;
import model.Transaction;

public class RemoveTransactionCommand implements Command {
    private Transaction transactionToRemove;
    private Account account;

    public RemoveTransactionCommand(Transaction transactionToRemove, Account account) {
        this.transactionToRemove = transactionToRemove;
        this.account = account;
    }

    @Override
    public String getName() {
        return "Remove transaction: " + transactionToRemove.toString();
    }

    @Override
    public void execute() {
        account.removeTransaction(transactionToRemove);
    }

    @Override
    public void undo() {
        // Przywrócenie usuniętej transakcji
        account.addTransaction(transactionToRemove);
    }

    @Override
    public void redo() {
        // Ponowne usunięcie tej samej transakcji
        account.removeTransaction(transactionToRemove);
    }
}
