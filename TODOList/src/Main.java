import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    private static ArrayList<Task> tasks = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);
    private static final String FILE_NAME = "tasks.dat";

    public static void main(String[] args) {
        System.out.println("Добро пожаловать в приложение TODO List!");

        // Загружаем задачи из файла при запуске программы
        loadTasksFromFile();

        boolean exit = false;
        while (!exit) {
            showMenu();
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    addTask();
                    break;
                case "2":
                    showTasks();
                    break;
                case "3":
                    markTaskAsCompleted();
                    break;
                case "4":
                    exit = true;
                    System.out.println("Выход из приложения. До свидания!");
                    break;
                default:
                    System.out.println("Неверный выбор. Пожалуйста, попробуйте снова.");
            }
        }
    }

    private static void showMenu() {
        System.out.println("\nВыберите действие:");
        System.out.println("1. Добавить задачу");
        System.out.println("2. Показать задачи");
        System.out.println("3. Отметить задачу как выполненную");
        System.out.println("4. Выход");
        System.out.print("Ваш выбор: ");
    }

    private static void addTask() {
        System.out.print("Введите описание задачи: ");
        String description = scanner.nextLine();
        Task task = new Task(description);
        tasks.add(task);
        System.out.println("Задача добавлена.");
        saveTasksToFile(); // Сохраняем задачи после добавления
    }

    private static void showTasks() {
        if (tasks.isEmpty()) {
            System.out.println("Список задач пуст.");
        } else {
            System.out.println("Список задач:");
            for (int i = 0; i < tasks.size(); i++) {
                Task task = tasks.get(i);
                String status = task.isCompleted() ? "[Выполнено]" : "[Не выполнено]";
                System.out.println((i + 1) + ". " + status + " " + task.getDescription());
            }
        }
    }

    private static void markTaskAsCompleted() {
        showTasks();
        System.out.print("Введите номер задачи для отметки как выполненной: ");
        int taskNumber;
        try {
            taskNumber = Integer.parseInt(scanner.nextLine());
            if (taskNumber > 0 && taskNumber <= tasks.size()) {
                Task task = tasks.get(taskNumber - 1);
                task.markAsCompleted();
                System.out.println("Задача отмечена как выполненная.");
                saveTasksToFile(); // Сохраняем задачи после изменения
            } else {
                System.out.println("Неверный номер задачи.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Пожалуйста, введите корректный номер задачи.");
        }
    }

    private static void saveTasksToFile() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            out.writeObject(tasks);
            System.out.println("Задачи сохранены в файл.");
        } catch (IOException e) {
            System.out.println("Ошибка при сохранении задач: " + e.getMessage());
        }
    }

    private static void loadTasksFromFile() {
        File file = new File(FILE_NAME);
        if (file.exists()) {
            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
                tasks = (ArrayList<Task>) in.readObject();
                System.out.println("Задачи загружены из файла.");
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Ошибка при загрузке задач: " + e.getMessage());
            }
        }
    }
}
