import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Pattern;

public class DataApp {

    private static final int EXPECTED_FIELDS_COUNT = 6;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Введите данные (фамилия имя отчество дата рождения номер телефона пол):");
        String input = scanner.nextLine();

        String[] fields = input.split(" ");

        try {
            validateFieldsCount(fields);
            validateFields(fields);

            String lastName = fields[0];
            String firstName = fields[1];
            String patronymic = fields[2];
            String birthDate = fields[3];
            long phoneNumber = Long.parseLong(fields[4]);
            char gender = fields[5].charAt(0);

            String dataLine = String.format("%s %s %s %s %d %c", lastName, firstName, patronymic, birthDate, phoneNumber, gender);

            writeFile(lastName, dataLine);

            System.out.println("Данные успешно записаны в файл " + lastName + ".txt");
        } catch (DataFormatException e) {
            System.err.println("Ошибка формата данных: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Ошибка формата номера телефона: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Ошибка записи в файл: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void validateFieldsCount(String[] fields) throws DataFormatException {
        if (fields.length != EXPECTED_FIELDS_COUNT) {
            throw new DataFormatException("Неверное количество данных. Ожидается " + EXPECTED_FIELDS_COUNT + " полей, получено " + fields.length);
        }
    }

    private static void validateFields(String[] fields) throws DataFormatException {
        Pattern datePattern = Pattern.compile("^\\d{2}\\.\\d{2}\\.\\d{4}$");
        Pattern genderPattern = Pattern.compile("^[fm]$");

        for (int i = 0; i < fields.length; i++) {
            if (fields[i].isEmpty()) {
                throw new DataFormatException("Поле " + (i + 1) + " не должно быть пустым");
            }

            if (i == 3 && !datePattern.matcher(fields[i]).matches()) {
                throw new DataFormatException("Дата рождения должна быть в формате dd.mm.yyyy");
            }

            if (i == 5 && !genderPattern.matcher(fields[i]).matches()) {
                throw new DataFormatException("Пол должен быть 'f' или 'm'");
            }
        }
    }

    private static void writeFile(String lastName, String dataLine) throws IOException {

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(lastName + ".txt", true))) {
            writer.write(dataLine + "\n");
        }
    }
}