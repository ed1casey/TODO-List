import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;

public class TodoListGUI {
    private JFrame frame;
    private DefaultListModel<Task> taskListModel;
    private JList<Task> taskJList;
    private JButton addButton;
    private JButton deleteButton;
    private JButton completeButton;
    private JTextField taskField;
    private static final String FILE_NAME = "tasks.dat";
    private ArrayList<Task> tasks;

    public TodoListGUI() {
        tasks = loadTasksFromFile();

        frame = new JFrame("TODO List");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(new BorderLayout());

        taskListModel = new DefaultListModel<>();
        for (Task task : tasks) {
            taskListModel.addElement(task);
        }

        taskJList = new JList<>(taskListModel);
        taskJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        taskJList.setCellRenderer(new TaskCellRenderer());

        JScrollPane scrollPane = new JScrollPane(taskJList);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BorderLayout());

        taskField = new JTextField();
        addButton = new JButton("Добавить");

        inputPanel.add(taskField, BorderLayout.CENTER);
        inputPanel.add(addButton, BorderLayout.EAST);

        JPanel buttonPanel = new JPanel();
        deleteButton = new JButton("Удалить");
        completeButton = new JButton("Выполнено");

        buttonPanel.add(completeButton);
        buttonPanel.add(deleteButton);

        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(inputPanel, BorderLayout.NORTH);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        // Добавляем обработчики событий
        addButton.addActionListener(e -> addTask());
        deleteButton.addActionListener(e -> deleteTask());
        completeButton.addActionListener(e -> completeTask());
        taskField.addActionListener(e -> addTask());

        frame.setVisible(true);
    }

    private void addTask() {
        String description = taskField.getText().trim();
        if (!description.isEmpty()) {
            Task task = new Task(description);
            tasks.add(task);
            taskListModel.addElement(task);
            taskField.setText("");
            saveTasksToFile();
        }
    }

    private void deleteTask() {
        int index = taskJList.getSelectedIndex();
        if (index != -1) {
            tasks.remove(index);
            taskListModel.remove(index);
            saveTasksToFile();
        } else {
            JOptionPane.showMessageDialog(frame, "Пожалуйста, выберите задачу для удаления.");
        }
    }

    private void completeTask() {
        int index = taskJList.getSelectedIndex();
        if (index != -1) {
            Task task = tasks.get(index);
            task.markAsCompleted();
            taskListModel.set(index, task); // Обновляем модель списка
            saveTasksToFile();
            taskJList.repaint(); // Перерисовываем список для обновления отображения
        } else {
            JOptionPane.showMessageDialog(frame, "Пожалуйста, выберите задачу для отметки как выполненную.");
        }
    }

    private void saveTasksToFile() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            out.writeObject(tasks);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Ошибка при сохранении задач: " + e.getMessage());
        }
    }

    private ArrayList<Task> loadTasksFromFile() {
        File file = new File(FILE_NAME);
        if (file.exists()) {
            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
                return (ArrayList<Task>) in.readObject();
            } catch (IOException | ClassNotFoundException e) {
                JOptionPane.showMessageDialog(frame, "Ошибка при загрузке задач: " + e.getMessage());
            }
        }
        return new ArrayList<>();
    }

    // Класс для кастомного отображения задач в списке
    private class TaskCellRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            Task task = (Task) value;
            if (task.isCompleted()) {
                setText("<html><strike>" + task.getDescription() + "</strike></html>");
            } else {
                setText(task.getDescription());
            }
            return this;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(TodoListGUI::new);
    }
}
