package org.example;

public class Course {
    private String courseCourse;
    private int courseAssessmentNr;
    private int studentNrCourse;
    private int mixedcourse;
    private int assessmentMandatoryNumberCourse ;
    private int percentageCourse ;

    // Constructor
    public Course(String courseCourse, int courseAssessmentNr, int studentNrCourse, int mixedcourse, int assessmentMandatoryNumberCourse, int percentageCourse) {
        this.courseCourse = courseCourse;
        this.courseAssessmentNr = courseAssessmentNr;
        this.studentNrCourse = studentNrCourse;
        this.mixedcourse = mixedcourse;
        this.assessmentMandatoryNumberCourse = assessmentMandatoryNumberCourse;
        this.percentageCourse = percentageCourse;
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

    public int getmixedcourse() {
        return mixedcourse;
    }

    public void setmixedcourse(int mixedcourse) {}

    public int getassessmentMandatoryNumberCourse() {
        return assessmentMandatoryNumberCourse;
    }
    public void setassessmentMandatoryNumberCourse(int assessmentMandatoryNumberCourse) {}

    public int getpercentageCourse() {
        return percentageCourse;
    }
    public void setpercentageCourse(int percentageCourse) {}

    @Override
    public String toString() {
        return courseCourse;  // Return the name of the course
    }
}
