package org.example;

public class Assessment {

    public String assessment_course_email_user;
    public String assessment_course_course;
    public String assessment_course_assessment;
    public int assessment_course_percentage;
    public String assessment_course_date;
    public int assessment_course_required_room;
    public int assessment_course_required_room_computer;
    public String assessment_course_room;
    public int assessment_course_mandatorty;


    public Assessment(String assessment_course_email_user, String assessment_course_course, String assessment_course_assessment, int assessment_course_percentage,
                      String assessment_course_date, int assessment_course_required_room, int assessment_course_required_room_computer
            , String assessment_course_room, int assessment_course_mandatorty) {

        this.assessment_course_email_user = assessment_course_email_user;
        this.assessment_course_course = assessment_course_course;
        this.assessment_course_assessment = assessment_course_assessment;
        this.assessment_course_percentage = assessment_course_percentage;
        this.assessment_course_date = assessment_course_date;
        this.assessment_course_required_room = assessment_course_required_room;
        this.assessment_course_required_room_computer = assessment_course_required_room_computer;
        this.assessment_course_room = assessment_course_room;
        this.assessment_course_mandatorty = assessment_course_mandatorty;

    }

    public String getAssessment_course_email_user() {
        return assessment_course_email_user;
    }

    public void setAssessment_course_email_user(String assessment_course_email_user) {
        this.assessment_course_email_user = assessment_course_email_user;
    }

    public String getAssessment_course_course() {
        return assessment_course_course;
    }

    public void setAssessment_course_course(String assessment_course_course) {
        this.assessment_course_course = assessment_course_course;
    }

    public String getAssessment_course_assessment() {
        return assessment_course_assessment;
    }

    public void setAssessment_course_assessment(String assessment_course_assessment) {
        this.assessment_course_assessment = assessment_course_assessment;
    }

    public int getAssessment_course_percentage() {
        return assessment_course_percentage;
    }

    public void setAssessment_course_percentage(int assessment_course_percentage) {
        this.assessment_course_percentage = assessment_course_percentage;
    }

    public String getAssessment_course_date() {
        return assessment_course_date;
    }

    public void setAssessment_course_date(String assessment_course_date) {
        this.assessment_course_date = assessment_course_date;
    }

    public int getAssessment_course_required_room() {
        return assessment_course_required_room;
    }

    public void setAssessment_course_required_room(int assessment_course_required_room) {
        this.assessment_course_required_room = assessment_course_required_room;
    }

    public int getAssessment_course_required_room_computer() {
        return assessment_course_required_room_computer;
    }

    public void setAssessment_course_required_room_computer(int assessment_course_required_room_computer) {
        this.getAssessment_course_required_room_computer();
    }

    public String getAssessment_course_room() {
        return assessment_course_room;
    }

    public void setAssessment_course_room(String assessment_course_room) {
        this.assessment_course_room = assessment_course_room;
    }

    public int getAssessment_course_mandatorty() {
        return assessment_course_mandatorty;
    }

    public void setAssessment_course_mandatorty(int assessment_course_mandatorty) {
        this.getAssessment_course_mandatorty();
    }

    //For the combobox

    @Override
    public String toString() {
        return assessment_course_date;
    }
}