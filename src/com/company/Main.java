package com.company;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.swing.filechooser.FileSystemView;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;


                                        //Полякова Юлия БАСО-02-18
                                        //Задача на получение информации о дисках, запись файла, работа с zip
                                        //работа с zip и xml
                                        //(первая практическая)


public class Main {

    public static void main(String[] args) {
        int c;
        do {
            System.out.println("Выберете действие");
            System.out.println("1. Вывести информацию о дисках");
            System.out.println("2. Запись файла");
            System.out.println("3. Работа в zip");
            System.out.println("4. Работа с xml");
            System.out.println("0. Выход");
            Scanner sc = new Scanner(System.in);
            c = sc.nextInt();
            switch (c) {
                case 1:
                    first();
                    break;
                case 2:
                    second();
                    break;

                case 3:
                    third();
                    break;

                case 4:
                    fourth();
                    break;

                case 0:
                    System.out.println("Выход");
                    break;
            }
        } while (c > 0);
    }


    private static void first() {
        Scanner sc = new Scanner(System.in);
        String Disk = sc.nextLine();
        File file = new File(Disk + ":");
        long tS = file.getTotalSpace() / (1024 * 1024);
        long fS = file.getFreeSpace() / (1024 * 1024);
        System.out.println("Disk " + Disk + ":");
        System.out.println("Общее пространство = " + tS + " MB");
        System.out.println("Свободное пространство = " + fS + " MB");


        System.out.println("Все доступные драйвера: ");
        FileSystemView fsv = FileSystemView.getFileSystemView();
        File[] drives = File.listRoots();
        if (drives != null && drives.length > 0) {
            for (File aDrive : drives) {
                System.out.println("Drive Letter: " + aDrive);
                System.out.println("\tType: " + fsv.getSystemTypeDescription(aDrive));
                System.out.println("\tВсё место: " + aDrive.getTotalSpace() / (1024 * 1024) + " MB");
                System.out.println("\tСвободное место: " + aDrive.getFreeSpace() / (1024 * 1024) + " MB");
                System.out.println();
            }
        }
    }


    private static void second(){
        try {
            File file = new File("fname.txt");
            if (file.createNewFile()) {
                System.out.println("Файл создан: " + file.getName());
            } else {
                System.out.println("Файл уже существует.");
                System.exit(0);
            }
        } catch (IOException e) {
            System.out.println("Ошибочка");
            e.printStackTrace();
        }

        try {
            FileWriter myWriter = new FileWriter("fname.txt");
            Scanner in = new Scanner(System.in);
            System.out.print("Пишите: ");
            String n = in.nextLine();
            in.close();
            myWriter.write(n);
            myWriter.close();
            System.out.println("Всё записано");
        } catch (IOException e) {
            System.out.println("Ошибочка");
            e.printStackTrace();
        }
        try {
            File file = new File("fname.txt");
            Scanner r = new Scanner(file);
            while (r.hasNextLine()) {
                String data = r.nextLine();
                System.out.println(data);
            }
            r.close();
        } catch (FileNotFoundException e) {
            System.out.println("Ошибочка");
            e.printStackTrace();
        }
        System.gc();
    }


    private static void third(){
        String filename = "fname.txt";
        try (ZipOutputStream zo = new ZipOutputStream(new FileOutputStream("output.zip"));
             FileInputStream fis = new FileInputStream(filename);) {
            ZipEntry e1 = new ZipEntry("fname.txt");
            zo.putNextEntry(e1);

            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);

            zo.write(buffer);

            zo.closeEntry();
        } catch (Exception ex) {

            System.out.println(ex.getMessage());
        }

        try(ZipInputStream zi = new ZipInputStream(new FileInputStream("new.zip")))

        {
            ZipEntry e;
            String n;
            while ((e = zi.getNextEntry()) != null) {

                n = e.getName();

                System.out.printf("Имя файла: %s \t \n", n);


                FileOutputStream fo = new FileOutputStream("новый" + n);
                for (int c = zi.read(); c != -1; c = zi.read()) {
                    fo.write(c);
                }
                fo.flush();
                zi.closeEntry();
                fo.close();
            }
        }
        catch(
                Exception e)
        {
            System.out.println(e.getMessage());
        }
        System.gc();
    }


    private static final String FILENAME = "staff.xml";
    private static void fourth() {
        try {
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();

            Element c = doc.createElement("Компания");
            doc.appendChild(c);

            Element sf = doc.createElement("Сотрудник");
            c.appendChild(sf);

            Attr id = doc.createAttribute("ID");
            id.setTextContent("1");
            sf.setAttributeNode(id);

            Element firn = doc.createElement("Имя");
            firn.setTextContent("Юлия");
            sf.appendChild(firn);

            Element surn = doc.createElement("Фамилия");
            surn.setTextContent("Юлия");
            sf.appendChild(surn);

            Element nic = doc.createElement("Группа");
            nic.setTextContent("БАСО");
            sf.appendChild(nic);

            Element sal = doc.createElement("Зарплата");
            sal.setTextContent("10000000000000");
            sf.appendChild(sal);

            Transformer trans = TransformerFactory.newInstance().newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(System.getProperty("user.dir") + File.separator + FILENAME));

            trans.transform(source, result);
            System.out.println("Документ создан");

        } catch (ParserConfigurationException | TransformerConfigurationException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }

        System.out.println("Изменить ли xml? Если да - введите 0");
        Scanner sc = new Scanner(System.in);
        String z = sc.nextLine();
        int f = Integer.parseInt(z);
        if (f == 0) {
            try {
                final String filepath = System.getProperty("user.dir") + File.separator + FILENAME;
                final File xmlFile = new File(filepath);
                DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                Document doc = db.parse(xmlFile);
                doc.normalize();
                Node c = doc.getFirstChild();
                Node staff = doc.getElementsByTagName("Сотрудник").item(0);
                NodeList nodeList = staff.getChildNodes();
                for (int i = 0; i < nodeList.getLength(); i++) {
                    Node nextNode = nodeList.item(i);

                    if (nextNode.getNodeName().equals("Зарплата")) {
                        nextNode.setTextContent("2000000");
                    }
                }
                Transformer transformer = TransformerFactory.newInstance().newTransformer();
                DOMSource source = new DOMSource(doc);
                StreamResult result = new StreamResult(new File(filepath));
                transformer.transform(source, result);
                System.out.println("Изменения сохранены");

            } catch (SAXException | IOException | ParserConfigurationException | TransformerConfigurationException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            } catch (TransformerException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}