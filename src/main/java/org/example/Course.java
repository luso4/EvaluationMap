package org.example;

public class Course {
    private String courseCourse;
    private int courseAssessmentNr;
    private int studentNrCourse;

    // Constructor
    public Course(String courseCourse, int courseAssessmentNr, int studentNrCourse) {
        this.courseCourse = courseCourse;
        this.courseAssessmentNr = courseAssessmentNr;
        this.studentNrCourse = studentNrCourse;
    }

    public String getcourseCourse() {
        return courseCourse;
    }

    public void setcourseCourse(String courseCourse) {
        this.courseCourse = courseCourse;
    }
    public int getstudentNrCourse() {
        return studentNrCourse;
    }

    public void setstudentNrCourse(int studentNrCourse) {
        this.studentNrCourse = studentNrCourse;
    }

    public int getcourseAssessmentNr() {
        return courseAssessmentNr;
    }

    public void setcourseAssessmentNr(int courseAssessmentNr) {
        this.courseAssessmentNr = courseAssessmentNr;
    }
}