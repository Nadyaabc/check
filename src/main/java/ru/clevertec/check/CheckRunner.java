package main.java.ru.clevertec.check;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.NumberFormat;
import java.util.*;

public class CheckRunner {
    public static void main(String[] args) {
        int argsLength = args.length;
        String balanceString;
        double balance = 0;
        int discountCardNumber = 0, discount = -1;
        boolean hasDiscountCard = false;
        double wholesaleDiscount = 0.1;
        //check if the arguments are valid
        try {
            //check if there is a right amount of args
            if (argsLength < 2) throw new IllegalArgumentException();

            if (argsLength == 2) {
                balanceString = args[1];
                if (!(balanceString.matches("balanceDebitCard=(-)?\\d+(.\\d{2})?") && args[0].matches("\\d+-\\d+"))) {
                    throw new IllegalArgumentException();
                } else {
                    balance = Double.parseDouble(balanceString.split("=")[1]);
                }
            }

            if (argsLength > 2) {
                balanceString = args[argsLength - 1];

                //check if balance is valid
                if (!balanceString.matches("balanceDebitCard=(-)?\\d+(.\\d{2})?")) {
                    throw new IllegalArgumentException();
                }

                //check if args[length-2] valid
                if (!(args[argsLength - 2].matches("\\d+-\\d+") || args[argsLength - 2].matches("discountCard=\\d{4}"))) {
                    throw new IllegalArgumentException();
                } else {
                    balance = Double.parseDouble(balanceString.split("=")[1]);
                    if (!args[argsLength - 2].matches("\\d+-\\d+")) {
                        discountCardNumber = Integer.parseInt(args[argsLength - 2].split("=")[1]);
                        hasDiscountCard = true;
                    }
                }
            }

        } catch (IllegalArgumentException e) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/result.csv"))) {
                System.out.println("ERROR\nBAD REQUEST\nIt seems that you made a mistake with your request.");
                System.out.println("---Example: java -cp ./src/main/java/ru/clevertec/check/CheckRunner 3-1 2-5 5-1 discountCard=1111 balanceDebitCard=100.10");
                writer.write("ERROR\nBAD REQUEST");
            } catch (IOException ee) {
                System.out.println("RESULT.CSV ERROR");
            }
            System.exit(1);
        }

        //check if discount card exists and get it disc
        if (hasDiscountCard) {
            try {
                List<String> linesDiscountCard = Files.readAllLines(Paths.get("./src/main/resources/discountCards.csv"));
                linesDiscountCard.removeFirst();
                for (String line : linesDiscountCard) {
                    String[] splitLine = line.split(";");
                    int cardNumber = Integer.parseInt(splitLine[1]);
                    if (cardNumber == discountCardNumber) {
                        discount = Integer.parseInt(splitLine[2]);
                    }
                }
                if (discount == -1) throw new Exception();
            } catch (IOException e) {
                System.out.println("INTERNAL SERVER ERROR");
                System.exit(1);
            } catch (Exception e) {
                try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/result.csv"))) {
                    System.out.println("ERROR\nBAD REQUEST");
                    System.out.println("It seems that your discount card number is wrong");
                    writer.write("ERROR\nBAD REQUEST");
                } catch (IOException ee) {
                    System.out.println("RESULT.CSV ERROR");
                }
                System.exit(1);
            }
        } else {
            discount = 0;
        }

        //add elements to hashmap
        HashMap<Integer, Integer> idQty = new HashMap<>();
        int id;
        try {
            for (int i = 0; i < argsLength - 2; i++) {
                id = Integer.parseInt((args[i].split("-"))[0]);
                int qty = Integer.parseInt((args[i].split("-"))[1]);
                idQty.computeIfPresent(id, (a, b) -> b + qty);
                idQty.putIfAbsent(id, qty);
            }
        } catch (NumberFormatException e) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/result.csv"))) {
                System.out.println("ERROR\nINTERNAL SERVER ERROR");
                writer.write("ERROR\nINTERNAL SERVER ERROR");
            } catch (IOException ee) {
                System.out.println("RESULT.CSV ERROR");
            }
            System.exit(1);
        }

        double total = 0, totalDiscount = 0, totalWithDiscount = 0;

        //current date and time
        Calendar c = Calendar.getInstance();
        String currentTime = (String.format("%02d", c.get(Calendar.HOUR_OF_DAY))) + ":" +
                String.format("%02d", c.get(Calendar.MINUTE)) + ":" +
                String.format("%02d", c.get(Calendar.SECOND));
        String currentDate = (String.format("%02d", c.get(Calendar.DAY_OF_MONTH))) + "." +
                String.format("%02d", c.get(Calendar.MONTH)) + "." +
                String.format("%02d", c.get(Calendar.YEAR));

        //write data into the file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/result.csv"))) {
            writer.write("Date;Time\n");
            writer.write(currentDate + ";" + currentTime + "\n");
            writer.write("\nQTY;DESCRIPTION;PRICE;DISCOUNT;TOTAL\n");
            System.out.print("Date;Time\n" + currentDate + ";" + currentTime + "\n" + "\nQTY;DESCRIPTION;PRICE;DISCOUNT;TOTAL\n");
            NumberFormat nf = NumberFormat.getInstance(Locale.FRANCE);
            List<String> linesProducts = Files.readAllLines(Paths.get("./src/main/resources/products.csv"));
            linesProducts.removeFirst();
            Set<Integer> keys = idQty.keySet();
            int quantityRequested, quantityAvailable;
            double price;
            for (Integer key : keys) {
                for (String line : linesProducts) {
                    String[] splitLine = line.split(";");
                    if (splitLine[0].equals(key.toString())) {
                        quantityRequested = idQty.get(Integer.parseInt(splitLine[0]));
                        quantityAvailable = Integer.parseInt(splitLine[3]);
                        price = nf.parse(splitLine[2]).doubleValue();

                        if (quantityAvailable < quantityRequested) throw new Exception();
                        writer.write(quantityRequested + ";" + splitLine[1] + ";" + String.format("%.02f", price) + "$;");
                        System.out.print(quantityRequested + ";" + splitLine[1] + ";" + String.format("%.02f", price) + "$;");
                        if (quantityRequested > 4 && splitLine[4].equals("+")) {
                            writer.write(String.format("%.02f", quantityRequested * price * wholesaleDiscount) + "$;");
                            writer.write(String.format("%.02f", quantityRequested * price) + "$;");
                            System.out.print(String.format("%.02f", quantityRequested * price * wholesaleDiscount) + "$;" + String.format("%.02f", quantityRequested * price) + "$;");
                            totalDiscount += quantityRequested * price * wholesaleDiscount;
                        } else {
                            String s = String.format("%.02f", quantityRequested * price * discount / 100) + "$;";
                            writer.write(s);
                            System.out.print(s);
                            s = String.format("%.02f", quantityRequested * price) + "$;";
                            writer.write(s);
                            System.out.print(s);
                            totalDiscount += quantityRequested * price * discount / 100;
                        }
                        total += quantityRequested * price;
                        writer.write("\n");
                        System.out.println();
                    }
                }
            }
            try {
                totalWithDiscount = total - totalDiscount;
                if (totalWithDiscount > balance) throw new Exception();
            } catch (Exception e) {
                try (BufferedWriter writer1 = new BufferedWriter(new FileWriter("src/result.csv"))) {
                    System.out.println("\nCANCELLED. \nSorry, your balance is not enough");
                    writer1.write("ERROR\nNOT ENOUGH MONEY");
                } catch (IOException ee) {
                    System.out.println("RESULT.CSV ERROR");
                }
                System.exit(1);
            }

            if (hasDiscountCard) {
                writer.write("\nDISCOUNT CARD;DISCOUNT PERCENTAGE\n");
                writer.write(discountCardNumber + ";" + discount + "%" + "\n");
                System.out.print("\nDISCOUNT CARD;DISCOUNT PERCENTAGE\n");
                System.out.print(discountCardNumber + ";" + discount + "%" + "\n");
            }

            writer.write("\nTOTAL PRICE;TOTAL DISCOUNT;TOTAL WITH DISCOUNT\n");
            String s = String.format("%.02f", total) + "$;" + String.format("%.02f", totalDiscount) + "$;" + String.format("%.02f", totalWithDiscount) + "$";
            writer.write(s);
            System.out.print("\nTOTAL PRICE;TOTAL DISCOUNT;TOTAL WITH DISCOUNT\n");
            System.out.print(s);
        } catch (IOException e) {
            System.out.println("INTERNAL SERVER ERROR");
        } catch (Exception e) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/result.csv"))) {
                System.out.println("\nERROR\nBAD REQUEST");
                System.out.println("We don't have enough items in stock");
                writer.write("ERROR\nBAD REQUEST");
            } catch (IOException ee) {
                System.out.println("RESULT.CSV ERROR");
            }
            System.exit(1);
        }
    }
}
