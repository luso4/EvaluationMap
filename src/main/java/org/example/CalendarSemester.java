package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import com.toedter.calendar.JDateChooser;

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
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(9, 2, 10, 10));

        // Inicializando os componentes de data
        startDateChooser = new JDateChooser();
        endDateChooser = new JDateChooser();
        examStartDateChooser = new JDateChooser();
        examEndDateChooser = new JDateChooser();
        appealExamStartDateChooser = new JDateChooser();
        appealExamEndDateChooser = new JDateChooser();
        specialExamStartDateChooser = new JDateChooser();
        specialExamEndDateChooser = new JDateChooser();
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

        add(new JLabel("Início da Época Especial:"));
        add(specialExamStartDateChooser);

        add(new JLabel("Fim da Época Especial:"));
        add(specialExamEndDateChooser);

        add(new JLabel("")); // Espaço vazio para alinhamento
        add(submitButton);

        // Listener para o botão de submissão
        submitButton.addActionListener(this::handleSubmission);

        setVisible(true);
    }

    private void handleSubmission(ActionEvent e) {
        if (!validateDates()) {
            return;
        }
        saveDatesToDatabase();
    }

    private boolean validateDates() {
        Date startDate = startDateChooser.getDate();
        Date endDate = endDateChooser.getDate();
        Date examStartDate = examStartDateChooser.getDate();
        Date examEndDate = examEndDateChooser.getDate();
        Date appealExamStartDate = appealExamStartDateChooser.getDate();
        Date appealExamEndDate = appealExamEndDateChooser.getDate();
        Date specialExamStartDate = specialExamStartDateChooser.getDate();
        Date specialExamEndDate = specialExamEndDateChooser.getDate();

        if (startDate == null || endDate == null || examStartDate == null || examEndDate == null ||
                appealExamStartDate == null || appealExamEndDate == null || specialExamStartDate == null ||
                specialExamEndDate == null) {
            JOptionPane.showMessageDialog(this, "Por favor, preencha todas as datas.", "Erro", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (endDate.before(new Date(startDate.getTime() + 30L * 24 * 60 * 60 * 1000))) {
            JOptionPane.showMessageDialog(this, "A data de fim do semestre deve ser pelo menos um mês após o início.", "Erro", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (examStartDate.before(startDate)) {
            JOptionPane.showMessageDialog(this, "A data de início da época normal de exames não pode ser antes do início do semestre.", "Erro", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (examEndDate.before(new Date(examStartDate.getTime() + 7L * 24 * 60 * 60 * 1000))) {
            JOptionPane.showMessageDialog(this, "A data de fim da época normal de exames deve ser pelo menos uma semana após o início.", "Erro", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (appealExamStartDate.before(examEndDate)) {
            JOptionPane.showMessageDialog(this, "A data de início da época de recurso não pode ser antes do fim da época normal de exames.", "Erro", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (appealExamEndDate.before(new Date(appealExamStartDate.getTime() + 14L * 24 * 60 * 60 * 1000))) {
            JOptionPane.showMessageDialog(this, "A data de fim da época de recurso deve ser pelo menos duas semanas após o início.", "Erro", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (specialExamStartDate.before(appealExamEndDate)) {
            JOptionPane.showMessageDialog(this, "A data de início da época especial não pode ser antes do fim da época de recurso.", "Erro", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (specialExamEndDate.before(new Date(specialExamStartDate.getTime() + 14L * 24 * 60 * 60 * 1000))) {
            JOptionPane.showMessageDialog(this, "A data de fim da época especial deve ser pelo menos duas semanas após o início.", "Erro", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
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

            saveDate(preparedStatement, "Início do Semestre", startDateChooser.getDate());
            saveDate(preparedStatement, "Fim do Semestre", endDateChooser.getDate());
            saveDate(preparedStatement, "Início da Época Normal de Exames", examStartDateChooser.getDate());
            saveDate(preparedStatement, "Fim da Época Normal de Exames", examEndDateChooser.getDate());
            saveDate(preparedStatement, "Início da Época de Recurso", appealExamStartDateChooser.getDate());
            saveDate(preparedStatement, "Fim da Época de Recurso", appealExamEndDateChooser.getDate());
            saveDate(preparedStatement, "Início da Época Especial", specialExamStartDateChooser.getDate());
            saveDate(preparedStatement, "Fim da Época Especial", specialExamEndDateChooser.getDate());

            JOptionPane.showMessageDialog(this, "Datas inseridas com sucesso no banco de dados!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao salvar as datas no banco de dados!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void saveDate(PreparedStatement preparedStatement, String description, Date date) throws SQLException {
        if (date != null) {
            java.sql.Date sqlDate = new java.sql.Date(date.getTime());
            preparedStatement.setString(1, description);
            preparedStatement.setDate(2, sqlDate);
            preparedStatement.executeUpdate();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(CalendarSemester::new);
    }
}
