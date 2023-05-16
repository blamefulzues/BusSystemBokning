/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.bussbokning;

import java.util.Scanner;
import java.util.function.Predicate;

public class BussBokning {
    private static final int antalPlatser = 21;
    private static final long[] platser = new long[antalPlatser];    
    private static final Scanner textInmatning = new Scanner(System.in);
    private static double prisKlass1 = 149.90;
    private static double prisKlass2 = 299.90;
    private static Predicate<Long> platsÄrLedig = personNmr -> personNmr == 0;
    
    public static void main(String[] args) {
        System.out.println("Hej och välkomen till detta bussbokningssystem");
        
        hanteraMeny();
    }
    
    private static void visaMeny() {
        System.out.println("Vänligen välj ett av följande alternativ:\n");
        System.out.println("[1] Bokning av plats");
        System.out.println("[2] Avbokning av plats");
        System.out.println("[3] Hitta bokning");
        System.out.println("[4] Beräkna vinst");
        System.out.println("[5] Personer över 18");
        
        System.out.print("\nInmatning: ");
    }
    
    private static void hanteraMeny() {
        while (true) {
            visaMeny();
            boolean resultat = hanteraMenyInmatning();
            
            if (resultat == true) {
                System.out.println("\n");
                continue;
            }
            
            System.out.println("\n\nVänligen välj ett tal mellan 1 och 5\n\n");
        }
    }
    
    private static boolean hanteraMenyInmatning() {
        String val = textInmatning.nextLine();
        
        if (val.equals("1")) {
            bokaPlats();
            return true;
        }
        
        if (val.equals("2")) {
            avbokaPlats();
            return true;
        }
        
        if (val.equals("3")) {
            hittaBokning();
            return true;
        }
        
        if (val.equals("4")) {
            vinstHantering();
            return true;
        }
        
        if (val.equals("5")) {
            visaKunderÖver18();
            return true;
        }
        
        return false;
    }
    
    private static int platsInmatning() {
        while (true) {
            System.out.print("\nInmatning: ");
            String resultat = hanteraPlatsInmatning();
            
            if (stringÄrNumerisk(resultat)) return Integer.parseInt(resultat);
            
            System.out.println(resultat);
        }
    }
    
    private static long personNmrInmatning() {
        while (true) {
            System.out.print("Vänligen ange ditt personnummer (ÅÅÅÅMMDD-XXXX): ");
            String personNmr = textInmatning.nextLine();
            
            boolean resultat = valideraPersonNmr(personNmr);
            
            if (resultat) {
                String personNmrUtanBindestreck = personNmr.substring(0, 8) + personNmr.substring(9, personNmr.length());
                return Long.parseLong(personNmrUtanBindestreck);
            }
            
            System.out.println("Vänligen följ gällande format \n");
        }
        
    }
    
    private static boolean valideraPersonNmr(String personNmr) {
        if (personNmr.length() != 13) return false;
        
        String[] delar = personNmr.split("-");
        
        if (delar.length != 2) return false;
        
        String vänsterLed = delar[0];
        String högerLed = delar[1];
        
        if (vänsterLed.length() != 8) return false;
        if (högerLed.length() != 4) return false;
        
        return true;
    }
    
    private static void bokaPlats() {
        System.out.println("Vänligen välj en av dessa lediga platser (om platsen är tagen visas ett X): \n");
        
        visaPlatsData();
        visaPlatser();
        int plats = platsInmatning() - 1;
        long personNmr = personNmrInmatning();
        
        platser[plats] = personNmr;
        
        System.out.println("Din plats i bussen är plats " + (plats + 1));
    }
    
    private static void hittaBokning() {
        if (platser.length == 0) {
            System.out.println("Det finns inga bokade platser");
            return;
        }
        
        long personNmr = personNmrInmatning();
        String bokadePlatser = visaBokadePlatserEnRad(platserFörPersonNmr(personNmr));
        
        System.out.println("Personen med personnummret: " + personNmr + " har bokat följande platser " + bokadePlatser);
    }
    
    private static String hanteraPlatsInmatning() {
        String plats = textInmatning.nextLine();
        
        if (stringÄrNumerisk(plats)) {
            int platsInt = Integer.parseInt(plats) - 1;
            
            if (platsInt < 0 || platsInt > 20) return "Välj plats mellan 1 och 21";
            
            boolean ledig = platsÄrLedig.test(platser[platsInt]);
            
            if (!ledig) return "Denna plats är tagen, vänligen välj en ny plats";
            
            return plats;
        }
        
        return "Vänligen mata in ett numerisk värde";
    }
    
    private static void visaKunderÖver18() {
        String resultat = "";
        
        long[] behandladePersonNummer = new long[antalPlatser];
        
        int index = 0;
        
        for (long personNmr : platser) {
            if (personNmr == 0) continue;
            
            if (finnsIArrayLong(behandladePersonNummer, personNmr)) continue;
            
            int ålder = årFrånPersonNmr(personNmr);
            
            if (ålder < 18) continue;
            
            String bokadePlatser = visaBokadePlatserEnRad(platserFörPersonNmr(personNmr));
            
            resultat += "\nPersonnummer: " + personNmr + ", Ålder: " + ålder + ", Platser: " + bokadePlatser;
            
            behandladePersonNummer[index++] = personNmr;
        }
        
        if (resultat.equals("")) {
            System.out.println("Det finns inga kunder över 18 år");
        }
        
        else {
            System.out.println(resultat);
        }
    }
    
    private static boolean stringÄrNumerisk(String str) {
        try {
            Integer.parseInt(str);
            
            return true;
        }
        
        catch(Throwable ex) {
            return false;   
        }
    }
    
    private static int beräknaLedigaPlatser() {
        int ledigaPlatser = 0;
        
        for (long personNmr : platser) {
            if (platsÄrLedig.test(personNmr)) ledigaPlatser += 1; 
        }
        
        return ledigaPlatser;
    }
    
    private static int beräknaUpptagnaPlatser() {
        int upptagnaPlatser = 0;
        
        for (long personNmr : platser) {
            if (!platsÄrLedig.test(personNmr)) upptagnaPlatser += 1; 
        }
        
        return upptagnaPlatser;
    }
    
    private static void visaPlatsData() {
        int ledigaPlatser = beräknaLedigaPlatser();
        int upptagnaPlatser = beräknaUpptagnaPlatser();
        
        System.out.println("Antal lediga platser: " + ledigaPlatser);
        System.out.println("Antal upptagna platser: " + upptagnaPlatser + "\n");
    }
    
    private static int[] platserFörPersonNmr(long personNmr) {
        int[] bokadePlatser = new int[antalPlatser];
        int antalBokningar = 0;
        
        for (int i = 0; i < antalPlatser; i++) {
            long nummer = platser[i];
            
            if (personNmr == nummer) {
                bokadePlatser[antalBokningar++] = i + 1;
            }
        }
        
        if (antalBokningar == 0) return null;
        
        return bokadePlatser;
    } 
    
    private static String visaBokadePlatser(int[] bokadePlatser) {
        String platserAttVisa = "";
        
        for (int plats : bokadePlatser) {
            if (plats == 0) break;
            
            platserAttVisa += plats + "\n";
        }
        
        return platserAttVisa;
    }
    
    private static String visaBokadePlatserEnRad(int[] bokadePlatser) {
        String platserAttVisa = "";
        
        for (int plats : bokadePlatser) {
            if (plats == 0) break;
            
            platserAttVisa += plats + ", ";
        }
        
        return platserAttVisa.substring(0, platserAttVisa.length() - 2);
    }
    
    private static boolean finnsIArray(int[] array, int värde) {
        if (värde == 0) return false;
        
        for (int annatVärde : array) {
            if (annatVärde == värde) return true;
        }
        
        return false;
    }
    
    private static boolean finnsIArrayLong(long[] array, long värde) {
        if (värde == 0) return false;
        
        for (long annatVärde : array) {
            if (annatVärde == värde) return true;
        }
        
        return false;
    }
    
    private static int platsInmatningFörAvbokning(int[] bokadePlatser) {
        while (true) {
            System.out.print("Inmatning: ");
            String plats = textInmatning.nextLine();
        
            if (!stringÄrNumerisk(plats)) {
                System.out.println("Vänligen välj ett nummmer för platsen du vill avboka");
                continue;
            }
            
            if (!finnsIArray(bokadePlatser, Integer.parseInt(plats))) {
                System.out.println("Vänligen välj en plats som är bokad för personnummret");
                System.out.println("Platserna du kan välja mellan är: ");
                System.out.println(visaBokadePlatser(bokadePlatser));
                
                continue;
            }
            
            return Integer.parseInt(plats) - 1;
        }
        
        
    }
    
    private static void avbokaPlats() {
        if (platser.length == 0) {
            System.out.println("Det finns inga bokade platser");
            return;
        }
        
        long personNmr = personNmrInmatning();
        int[] bokadePlatser = platserFörPersonNmr(personNmr);
        
        if (bokadePlatser == null) {
            System.out.println("Det finns inga bokningar för personnummret: " + personNmr);
            return;
        } 
        
        System.out.println("Här är följande platser för personnummer: " + personNmr);
        System.out.println(visaBokadePlatser(bokadePlatser));
        
        int plats = platsInmatningFörAvbokning(bokadePlatser);
        
        platser[plats] = 0;
        
        System.out.println("Plats: " + (plats + 1) + " för personnummer: " + personNmr + " har avbokats");
    }
    
    private static void visaPlatser() {
        String platsVisualisering = "";
        
        for (int i = 0; i < platser.length; i++) {
            int plats = i + 1;
            long personNmr = platser[i];
            boolean ledig = platsÄrLedig.test(personNmr);
            
            String korridår = (i == 2 || i == 6 || i == 10 || i == 14) ? "|   |" : "";
            String nyRad = (plats % 4 == 0 && i != 0 && plats != 20) ? "\n" : "";
            String separator = (plats >= 10 && ledig) ? "|" : " |";
            String platsSymbol = ledig ? Integer.toString(i + 1) : "X";
            platsVisualisering += korridår + "| " + platsSymbol + separator + nyRad;
        }
        
        System.out.println(platsVisualisering);
    }
    
    private static void vinstHantering() {
        double vinst = beräknaVinst(0);
        
        String formateradVinst = vinstForrmatering(vinst);
        
        System.out.println("Vinsten för denna bussresa är: ");
        System.out.println("$" + formateradVinst);
    }
    
    private static String vinstForrmatering(double pris) {
        String prisStr = Double.toString(pris);
        String[] delar = prisStr.split("\\.");
        
        String resultat = "";
        
        int loops = 1;
       
        for (int i = delar[0].length() - 1; i >= 0; i--) {
            String siffra = delar[0].substring(i, i + 1);
            resultat = siffra + resultat;
            
            if (loops == 3 && i!= 0) {
                resultat = "," + resultat;
                loops = 1;
            }
            
            loops++;
        }
        
        return resultat + "." + kortaNerString(delar[1], 3);
    }
    
    private static int årFrånPersonNmr(long personNmr) {
        String årDel = Long.toString(personNmr).substring(0, 4);
        
        return 2023 - (int)Long.parseLong(årDel);
    }
    
    private static double prisFrånPersonNmr(long personNmr) {
        int ålder = årFrånPersonNmr(personNmr);
        
        if (ålder < 18 || ålder >= 69) {
            return prisKlass1;
        }
        
        return prisKlass2;
    }
    
    private static double beräknaVinst(int index) {
        if (index == antalPlatser) return 0;
        
        long personNmr = platser[index];
        
        if (platsÄrLedig.test(personNmr)) return 0 + beräknaVinst(index + 1);
        
        double pris = prisFrånPersonNmr(personNmr);
        
        return pris + beräknaVinst(index + 1);
    }
    
    private static String kortaNerString(String str, int tecken) {
        if (str.length() <= tecken) return str;
        
        return str.substring(0, tecken - 1);
    }
    
    private static boolean ärPlatsenBokad(int index) {
        return platser[index] != 0;
    }
}
