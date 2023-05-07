import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Main {
    public static void main(String[] args) {
        String dateRegex = "^\\d{4}\\-(0[1-9]|1[012])\\-(0[1-9]|[12][0-9]|3[01])$";

        Scanner scan = new Scanner(System.in);
        System.out.println("Введіть будь-ласка дату в форматі YYYY-MM-DD");
        String date = scan.nextLine();

        if (!date.matches(dateRegex)){
            System.out.println("Невірна дата!");
            System.exit(0);
        }

        System.out.println("Дата - " + date + ". Введіть код головної валюти:");
        String valMain = scan.nextLine();

        System.out.println("Головна валюта - " + valMain + ". Введіть код валюти конвертації:");
        String valConv = scan.nextLine();
        scan.close();

        System.out.println("Шукаємо курс " + valMain + " до " + valConv + " станом на " + date);

        try {

            String apiUrl = "https://cdn.jsdelivr.net/gh/fawazahmed0/currency-api@1/" + date + "/currencies/" + valMain + "/" + valConv + ".json";
            URL url = new URL(apiUrl);

            //Открываем коннекшн и делаем ГЕТ запрос по апишнику
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            //Проверяем респонс
            int responsecode = conn.getResponseCode();

            if (responsecode != 200) {
                throw new RuntimeException("HttpResponseCode: " + responsecode);
            } else {

                String inline = "";
                Scanner scanner = new Scanner(url.openStream());

                //Пишем Джсон в строку
                while (scanner.hasNext()) {
                    inline += scanner.nextLine();
                }

                scanner.close();

                //Парсим строку в Джсон объект
                JSONParser parse = new JSONParser();
                JSONObject data_obj = (JSONObject) parse.parse(inline);

                //Получаем курс
                Double obj = (Double) data_obj.get(valConv);

                //Выводим результат
                System.out.println("1 " + valMain + " = " + obj + " " + valConv);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}