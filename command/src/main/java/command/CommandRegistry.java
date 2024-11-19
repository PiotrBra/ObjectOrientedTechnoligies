package command;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class CommandRegistry {

	private ObservableList<Command> commandStack = FXCollections.observableArrayList();
	private ObservableList<Command> redoStack = FXCollections.observableArrayList();

	// Wykonanie komendy
	public void executeCommand(Command command) {
		// Wykonanie komendy
		command.execute();

		// Dodanie komendy do stosu wykonanych komend
		commandStack.add(command);

		// Czyszczenie stosu redo, ponieważ nowe działanie usuwa możliwość ponownego wykonania cofniętych operacji
		redoStack.clear();
	}

	// Cofanie ostatniej operacji
	public void undo() {
		if (!commandStack.isEmpty()) {
			Command lastCommand = commandStack.remove(commandStack.size() - 1);  // Pobranie ostatniej komendy
			lastCommand.undo();  // Cofnięcie operacji
			redoStack.add(lastCommand);  // Dodanie komendy do stosu redo
		}
	}

	// Ponowne wykonanie ostatniej operacji
	public void redo() {
		if (!redoStack.isEmpty()) {
			Command lastUndoneCommand = redoStack.remove(redoStack.size() - 1);  // Pobranie ostatniej cofniętej komendy
			lastUndoneCommand.redo();  // Ponowne wykonanie operacji
			commandStack.add(lastUndoneCommand);  // Dodanie komendy z powrotem do stosu wykonanych komend
		}
	}

	// Zwrócenie stosu komend
	public ObservableList<Command> getCommandStack() {
		return commandStack;
	}

	// Zwrócenie stosu cofniętych komend
	public ObservableList<Command> getRedoStack() {
		return redoStack;
	}
}
