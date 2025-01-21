package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import com.toedter.calendar.JDateChooser;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CalendarSemester extends JFrame {
    private JDateChooser startDateChooser;
    private JDateChooser endDateChooser;
    private JDateChooser examStartDateChooser;
    private JDateChooser examEndDateChooser;
    private JDateChooser appealExamStartDateChooser;
    private JDateChooser appealExamEndDateChooser;
    private JDateChooser specialExamStartDateChooser;
    private JDateChooser specialExamEndDateChooser;
    private JButton submitButton;

    public CalendarSemester() {
        setTitle("Definir Datas do Semestre");
        setSize(600, 500); // Ajustado para acomodar os novos campos
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(9, 2, 10, 10)); // Ajustado para mais linhas

        // Inicializando os componentes de data
        startDateChooser = new JDateChooser();
        endDateChooser = new JDateChooser();
        examStartDateChooser = new JDateChooser();
        examEndDateChooser = new JDateChooser();
        appealExamStartDateChooser = new JDateChooser();
        appealExamEndDateChooser = new JDateChooser();
        specialExamStartDateChooser = new JDateChooser(); // Novo campo
        specialExamEndDateChooser = new JDateChooser(); // Novo campo
        submitButton = new JButton("Confirmar Datas");

        // Adicionando rótulos e campos à interface
        add(new JLabel("Data de Início do Semestre:"));
        add(startDateChooser);

        add(new JLabel("Data de Fim do Semestre:"));
        add(endDateChooser);

        add(new JLabel("Início da Época Normal de Exames:"));
        add(examStartDateChooser);

        add(new JLabel("Fim da Época Normal de Exames:"));
        add(examEndDateChooser);

        add(new JLabel("Início da Época de Recurso:"));
        add(appealExamStartDateChooser);

        add(new JLabel("Fim da Época de Recurso:"));
        add(appealExamEndDateChooser);

        add(new JLabel("Início da Época Especial de Exames:")); // Novo campo
        add(specialExamStartDateChooser);

        add(new JLabel("Fim da Época Especial de Exames:")); // Novo campo
        add(specialExamEndDateChooser);

        add(new JLabel("")); // Espaço vazio para alinhamento
        add(submitButton);

        // Listener para o botão de submissão
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveDatesToDatabase();
            }
        });

        setVisible(true);
    }

    private void saveDatesToDatabase() {
        // Configuração da conexão com o banco de dados
        String url = "jdbc:mariadb://192.168.76.151:3306/evaluationmap"; // Substitua pelo nome do seu banco de dados
        String username = "userSQL"; // Substitua pelo seu usuário
        String password = "password1"; // Substitua pela sua senha

        // Consulta SQL para inserir as datas
        String insertQuery = "INSERT INTO tabela_das_datas (descricao, data) VALUES (?, ?)";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {

            // Inserir cada data com uma descrição
            saveDate(preparedStatement, "Início do Semestre", startDateChooser.getDate());
            saveDate(preparedStatement, "Fim do Semestre", endDateChooser.getDate());
            saveDate(preparedStatement, "Início da Época Normal de Exames", examStartDateChooser.getDate());
            saveDate(preparedStatement, "Fim da Época Normal de Exames", examEndDateChooser.getDate());
            saveDate(preparedStatement, "Início da Época de Recurso", appealExamStartDateChooser.getDate());
            saveDate(preparedStatement, "Fim da Época de Recurso", appealExamEndDateChooser.getDate());
            saveDate(preparedStatement, "Início da Época Especial de Exames", specialExamStartDateChooser.getDate()); // Novo campo
            saveDate(preparedStatement, "Fim da Época Especial de Exames", specialExamEndDateChooser.getDate()); // Novo campo

            JOptionPane.showMessageDialog(this, "Datas inseridas com sucesso no banco de dados!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao salvar as datas no banco de dados!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void saveDate(PreparedStatement preparedStatement, String description, java.util.Date date) throws SQLException {
        if (date != null) {
            java.sql.Date sqlDate = new java.sql.Date(date.getTime());
            preparedStatement.setString(1, description);
            preparedStatement.setDate(2, sqlDate);
            preparedStatement.executeUpdate();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CalendarSemester());
    }
}
