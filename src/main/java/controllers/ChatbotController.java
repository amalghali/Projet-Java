package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.application.Platform;

public class ChatbotController {

    @FXML private TextArea chatArea;
    @FXML private TextField txtInput;

    @FXML
    public void initialize() {
        chatArea.appendText("Assistant IA Expert Java: Bonjour Soldat ! Je suis votre instructeur technique. \nPosez-moi n'importe quelle question sur Java ou la mission Battle.\n\n");
    }

    @FXML
    private void handleSend() {
        String msg = txtInput.getText();
        if (msg != null && !msg.trim().isEmpty()) {
            chatArea.appendText("Moi: " + msg + "\n");
            txtInput.clear();
            
            new Thread(() -> {
                try { Thread.sleep(500); } catch (Exception e) {}
                Platform.runLater(() -> {
                    String resp = getExpertJavaResponse(msg);
                    chatArea.appendText("\nAssistant IA: " + resp + "\n\n");
                    chatArea.setScrollTop(Double.MAX_VALUE);
                });
            }).start();
        }
    }

    private String getExpertJavaResponse(String msg) {
        String l = msg.toLowerCase();
        
        // Salutations
        if (l.contains("bonjour") || l.contains("salut") || l.contains("hello")) {
            return "Bonjour ! Je suis ravi de vous aider. Comment puis-je vous assister dans votre apprentissage de Java ou dans votre mission de combat aujourd'hui ?";
        }
        
        // Expertise Java
        if (l.contains("jvm")) return "La JVM (Java Virtual Machine) est le cœur de Java. Elle compile le bytecode en code machine, \nce qui permet l'indépendance de la plateforme (WORA: Write Once, Run Anywhere).";
        
        if (l.contains("héritage") || l.contains("heritage")) return "L'héritage permet à une classe d'acquérir les propriétés d'une autre classe. \nC'est un pilier de l'Orienté Objet pour la réutilisation du code.";
        
        if (l.contains("polymorphisme")) return "Le polymorphisme permet d'utiliser un seul symbole pour représenter plusieurs types différents. \nEn Java, cela se fait via l'overriding (redéfinition) et l'overloading (surcharge).";
        
        if (l.contains("encapsulation")) return "L'encapsulation consiste à cacher les détails d'implémentation d'un objet et à \nne laisser l'accès qu'à travers des méthodes publiques (Getters/Setters).";
        
        if (l.contains("abstraction")) return "L'abstraction permet de se concentrer sur l'essentiel d'un objet. \nOn utilise des classes abstraites ou des interfaces pour définir des contrats de comportement.";
        
        if (l.contains("garbage collector")) return "Le Garbage Collector est le mécanisme automatique de Java qui libère la mémoire \nen supprimant les objets qui ne sont plus référencés. Plus besoin de 'free()' comme en C !";

        if (l.contains("java") || l.contains("code")) return "Java est un langage fortement typé. Pour devenir un expert, \nmaîtrisez les collections (List, Map) et les nouveautés des Streams de Java 8+.";

        // Battle
        if (l.contains("battle") || l.contains("drone")) return "Votre mission est de neutraliser le drone. Utilisez les flèches pour vous déplacer \net répondez correctement aux questions pour faire feu avec votre fusil d'assaut !";

        return "C'est une question technique pertinente sur '" + msg + "'. En tant qu'expert Java, \nje peux vous dire que ce concept est crucial pour optimiser vos performances. \nVoulez-vous un exemple de code ?";
    }
}
