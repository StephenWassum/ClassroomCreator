package kindergarten;

import org.w3c.dom.Node;

    /**
     * The Methods in this public class are utilized Driver.java    
     */

public class Classroom {
    private SNode studentsInLine;          
    private SNode musicalChairs;             
    private boolean[][] seatingAvailability;  
    private Student[][] studentsSitting;     

    /**
     * @param l passes in students in line
     * @param m passes in musical chairs
     * @param a passes in availability
     * @param s passes in students sitting
     */
    public Classroom ( SNode l, SNode m, boolean[][] a, Student[][] s ) {
		studentsInLine      = l;
        musicalChairs       = m;
		seatingAvailability = a;
        studentsSitting     = s;
	}
    /**
     * Default constructor starts an empty classroom. Do not edit.
     */
    public Classroom() {
        this(null, null, null, null);
    }

    /**
     * This method simulates students coming into the classroom and standing in line.
     * 
     * Reads students from input file and inserts these students in alphabetical 
     * order to studentsInLine singly linked list.
     * 
     * Input file contains 1st line with the number of students in the classroom,
     * The following lines contain the students 1st and last name and their age
     * 
     * @param filename the student information input file
     */
    public void makeClassroom ( String filename ) {

        // Read from file
        StdIn.setFile(filename);
        int ClassSize = Integer.parseInt(StdIn.readLine());

        // Initialize list of student objects from file.
        Student[] students = new Student[ClassSize];
        for (int i = 0; i < ClassSize; i++) {
            String student = StdIn.readLine();
            String[] FirstLastHeight = student.split("\\s+");
            students[i] = new Student(FirstLastHeight[0], FirstLastHeight[1], Integer.parseInt(FirstLastHeight[2]));
        }

        // Sort list of objects alphabetically.
        for (int i = 0; i < students.length; i++) {
            for (int j = 0; j < students.length; j++) {
                if(students[i].compareNameTo(students[j]) < 0){
                    Student lattername = students[i];
                    students[i] = students[j];
                    students[j] = lattername;
                }
            }
        }

        // Put list of Students into a linked list of SNodes
        studentsInLine = new SNode(students[0], null); 
        for (int i = 0; i < students.length; i++) {
            if (i == 0){
                studentsInLine = new SNode(students[i], null);
            }
            else{
                SNode nextNode = new SNode(students[i],null);
                SNode last = studentsInLine;
                while(last.getNext() != null){
                    last = last.getNext();
                }
                last.setNext(nextNode);
            }
        }
    }

    /**
     * 
     * This method reads a boolean array from an input file
     * 
     * The input file must contain a 1st ine with the number of rows, a second line with 
     * the number of columns, and then a boolean array.
     * 
     * From the input file this method creates a an array indicating the seating availability
     * in the classroom being built
     * 
     * @param seatingChart the seating chart input file
     */
    public void setupSeats(String seatingChart) {

        //Read from File
        StdIn.setFile(seatingChart);
        int DeskRows = StdIn.readInt();
        int DeskColumns = StdIn.readInt();

        //creating filled a boolean array indicating what seats are available
        seatingAvailability = new boolean [DeskRows][DeskColumns];
        for (int i = 0; i < DeskRows; i++) {
            for (int j = 0; j < DeskColumns; j++) {
                seatingAvailability[i][j] = StdIn.readBoolean();
            }
        }

        //creating an empty array of student objects to be filled later.
        studentsSitting = new Student [DeskRows][DeskColumns];
    }

    /**
     * 
     * Using setupseats and make classroom this method takes students from the classroom being made
     * and places them in seats according to the availbility created by setupseats.
     */
    public void seatStudents () {

        // checks if musical chairs isnt empty and fills first available seat in sitting array if not empty
        musicalChairsCheck:{
        for (int i = 0; i < seatingAvailability.length; i++) {
            for (int j = 0; j < seatingAvailability[0].length; j++) {
                if(seatingAvailability[i][j] == true && musicalChairs != null){
                    studentsSitting[i][j] = musicalChairs.getStudent();
                    //musicalChairs = new SNode(null, null);
                    break musicalChairsCheck;
                }
                else if (seatingAvailability[i][j] == true && musicalChairs == null){
                    break musicalChairsCheck;
                }
            }
        }}
        
        // Takes students in Students in line, removes and puts them in studentsitting array. 
        for (int i = 0; i < seatingAvailability.length; i++) {
            for (int j = 0; j < seatingAvailability[0].length; j++) {
                if(seatingAvailability[i][j] == true && studentsSitting[i][j] == null && studentsInLine != null){
                    studentsSitting[i][j] = studentsInLine.getStudent();
                    studentsInLine = studentsInLine.getNext();
                }
                if(studentsInLine == null){break;}
            }
        }
    }

    /**
     * This methods creates a linked list musical chairs to be used to play musical chairs,
     * in the playmusicalchairs method.
     */
    public void insertMusicalChairs () {

        // creates a node to point to front of musicalChairs.
        // sets front and creates a list from a singularly linked list from students sitting, removing from that array along the way.
        // makes the last node of musicalChairs point to the front.
        SNode First = new SNode();

        for (int i = 0; i < studentsSitting.length; i++) {
            for (int j = 0; j < studentsSitting[0].length; j++) {
                if(studentsSitting[i][j] != null && musicalChairs == null){
                    musicalChairs = new SNode(studentsSitting[i][j], null);
                    First = musicalChairs;
                    studentsSitting[i][j] = null;
                }
                else if (studentsSitting[i][j] != null){
                    SNode nextNode = new SNode(studentsSitting[i][j], null);
                    SNode last = musicalChairs;
                    musicalChairs = nextNode;
                    while(last.getNext() != null){
                        last = last.getNext();
                    }
                    last.setNext(musicalChairs);
                    studentsSitting[i][j] = null;
                }
            }
        }

        musicalChairs.setNext(First);
    }
     

    /**
     * 
     * This method simulates a game of musical chairs being played with the created classrom. 
     * Students are removed from the musical chairs list randomly until their is only 1 student left
     * This student is declared the winner and placed at the front of the student seating list
     * The rest of the students are placed in the seating list according to height order,
     * shortest to tallest.
     * 
     */
    public void playMusicalChairs() {

        // list count is number of students playing musical chairs/ in muscial chairs
        int listcount = 1;
        SNode lastNode = musicalChairs;

 
        //count how many kids in musical chairs
        while(lastNode.getNext() != musicalChairs){
            listcount += 1;
            lastNode = lastNode.getNext();
        }

        // print for check, makes list to put students in after removal for height processing
        // chair removal is what chair in game will be removed, prevNode because I was confused writing below funcions
        int ClassSize = listcount;
        Student[] students = new Student[ClassSize-1];
        int chairRemoval = StdRandom.uniform(listcount);
        SNode prevNode = lastNode;
        lastNode = musicalChairs;
        int studentcount = 0;

        // checks that class size is greater than 1 and runs the loop until it is
        while(studentcount != ClassSize-1){
            
            // counts through musical chairs until it finds the person selected to lose
            // then takes that students and puts them in a students list at the id of the first loop iteration
            // loop iteration for outside while loop is tracked by students count inner is NodeCounter.
            int NodeCounter = 0;
            SNode NodeTraveler = musicalChairs;
            System.out.println(chairRemoval);
            while(NodeCounter <= chairRemoval) {
                prevNode = NodeTraveler;
                NodeTraveler = NodeTraveler.getNext();
                NodeCounter ++;
            }
            Student loser = NodeTraveler.getStudent();
            //check if musicalChairs is being removed
            if(NodeTraveler == musicalChairs){
                musicalChairs = prevNode;
            }
            students[studentcount] = loser;
            prevNode.setNext(NodeTraveler.getNext());

            studentcount += 1;
            listcount --;
            if (listcount != 0) {chairRemoval = StdRandom.uniform(listcount);}
            StdOut.print(loser.print());
            System.out.println(listcount);
            System.out.println("  ");
        }

        //puts the kids in height order before placing them in line smallest to largest
        for (int i = 0; i < students.length; i++) {
            for (int j = 0; j < students.length; j++) {
                if( students[j] != null && students[i] != null && students[i].getHeight() < students[j].getHeight() ){
                    Student tiny = students[j];
                    students[j] = students[i];
                    students[i] = tiny;
                }
            }
        }

        // puts students sorted by height in line.
        studentsInLine = new SNode(students[0], null); 
        for (int i = 0; i < students.length; i++) {
            if (i == 0){
                studentsInLine = new SNode(students[i], null);
            }
            else{
                SNode nextNode = new SNode(students[i],null);
                SNode last = studentsInLine;
                while(last.getNext() != null){
                    last = last.getNext();
                }
                last.setNext(nextNode);
            }
        }

        // puts students in their seats
        seatStudents();
    } 

    /**
     * This method inserts a new (late) student to the next availible empty seat.
     * 
     */
    public void addLateStudent ( String firstName, String lastName, int height ) {

        // creates new student
        Student newStudent = new Student(firstName, lastName, height);

        //puts student at back of students in line
        if (studentsInLine != null){
            SNode nextNode = new SNode(newStudent, null);
            SNode last = studentsInLine;
            while(last.getNext() != null){
                last = last.getNext();
            }
            last.setNext(nextNode);
        }

        // puts student at back of musical chairs / to musical chairs node
        else if(musicalChairs != null && musicalChairs.getNext() != musicalChairs){
            SNode First = new SNode(musicalChairs.getNext().getStudent(), musicalChairs.getNext().getNext());
            SNode last = musicalChairs;
            musicalChairs = new SNode(newStudent, First);
            last.setNext(musicalChairs);
        }

        //if students not in line or musical chairs puts student in first available seat.
        else{
            AddNewStudentToSeatCheck: {
            for (int i = 0; i < studentsSitting.length; i++) {
                for (int j = 0; j < studentsSitting[0].length; j++) {
                    if(seatingAvailability[i][j]== true && studentsSitting[i][j] == null){
                        studentsSitting[i][j] = newStudent;
                        break AddNewStudentToSeatCheck;
                    }
                }
            }
        }
        } 
    }

    /**
     * If a student leaves the classroom early this method will delete them from our seating array.
     */
    public void deleteLeavingStudent ( String firstName, String lastName ) {

        // checks if students are in line
        if(studentsInLine != null){
            SNode check = studentsInLine;
            SNode prev = new SNode();

            // if student is first in line makes the first student in line the next to delete.
            if(studentsInLine.getStudent().getFirstName().equalsIgnoreCase(firstName) && studentsInLine.getStudent().getLastName().equalsIgnoreCase(lastName)){
                prev.setNext(studentsInLine.getNext());
                studentsInLine = studentsInLine.getNext();
                return;
            }
        
            // if student is in line and not last finds them and deletes them
            while(check.getNext() != null){
                Student studentCheck = check.getStudent();
                if(studentCheck.getFirstName().equalsIgnoreCase(firstName) && studentCheck.getLastName().equalsIgnoreCase(lastName)){
                    prev.setNext(check.getNext());
                    return;
                }
                prev = check;
                check = check.getNext();
            }

            // code for if student is the last in line, if they are it deletes them.
            if(check.getStudent().getFirstName().equalsIgnoreCase(firstName) && check.getStudent().getLastName().equalsIgnoreCase(lastName)){
                prev.setNext(check.getNext());
            }
        }

        // if students not in line checks musical chairs
        else if(musicalChairs != null && musicalChairs.getNext() != musicalChairs){
            SNode check = musicalChairs;
            SNode prev = new SNode();

            // if sutdent is last in musical chairs deletes them and repoints to front
            
           // if students is somewhere else in musical chairs deletes them. Also checks to make sure musical chairs isnt equal to check further code deletes musical chairs.
            while(check.getNext() != musicalChairs){
                Student studentCheck = check.getStudent();
                if(studentCheck.getFirstName().equalsIgnoreCase(firstName) && studentCheck.getLastName().equalsIgnoreCase(lastName) && check != musicalChairs){
                    prev.setNext(check.getNext());
                    return;
                }
                prev = check;
                check = check.getNext();
            }

            // if above circles through check will be variable before musical chairs. Hopefully this does it
            if(musicalChairs.getStudent().getFirstName().equalsIgnoreCase(firstName) && musicalChairs.getStudent().getLastName().equalsIgnoreCase(lastName)){
                check.setNext(musicalChairs.getNext());
                musicalChairs = check;
                return;
            }
        }

        // if student is winner of musical chairs / alone in musical chairs deletes them.
        else if (musicalChairs != null && musicalChairs.getNext() == musicalChairs){
            musicalChairs = new SNode(null, null);
        }

        // if students not in line or chairs looks in seats for student 
        // if student is found sitting deletes them.
        else{
            for (int i = 0; i < studentsSitting.length; i++) {
                for (int j = 0; j < studentsSitting[0].length; j++) {
                    if(seatingAvailability[i][j] == true && studentsSitting[i][j] != null){
                        if(studentsSitting[i][j].getFirstName().equalsIgnoreCase(firstName) && studentsSitting[i][j].getLastName().equalsIgnoreCase(lastName)){
                            studentsSitting[i][j] = null;
                            break;
                        }
                    }
                }
            }
        }
    }

    /**
     * Used by driver to display students in line
     */
    public void printStudentsInLine () {

        //Print studentsInLine
        StdOut.println ( "Students in Line:" );
        if ( studentsInLine == null ) { StdOut.println("EMPTY"); }

        for ( SNode ptr = studentsInLine; ptr != null; ptr = ptr.getNext() ) {
            StdOut.print ( ptr.getStudent().print() );
            if ( ptr.getNext() != null ) { StdOut.print ( " -> " ); }
        }
        StdOut.println();
        StdOut.println();
    }

    /**
     * Prints the seated students; can use this method to debug.
     */
    public void printSeatedStudents () {

        StdOut.println("Sitting Students:");

        if ( studentsSitting != null ) {
        
            for ( int i = 0; i < studentsSitting.length; i++ ) {
                for ( int j = 0; j < studentsSitting[i].length; j++ ) {

                    String stringToPrint = "";
                    if ( studentsSitting[i][j] == null ) {

                        if (seatingAvailability[i][j] == false) {stringToPrint = "X";}
                        else { stringToPrint = "EMPTY"; }

                    } else { stringToPrint = studentsSitting[i][j].print();}

                    StdOut.print ( stringToPrint );
                    
                    for ( int o = 0; o < (10 - stringToPrint.length()); o++ ) {
                        StdOut.print (" ");
                    }
                }
                StdOut.println();
            }
        } else {
            StdOut.println("EMPTY");
        }
        StdOut.println();
    }

    /**
     * Prints the musical chairs; can use this method to debug.
     */
    public void printMusicalChairs () {
        StdOut.println ( "Students in Musical Chairs:" );

        if ( musicalChairs == null ) {
            StdOut.println("EMPTY");
            StdOut.println();
            return;
        }
        SNode ptr;
        for ( ptr = musicalChairs.getNext(); ptr != musicalChairs; ptr = ptr.getNext() ) {
            StdOut.print(ptr.getStudent().print() + " -> ");
        }
        if ( ptr == musicalChairs) {
            StdOut.print(musicalChairs.getStudent().print() + " - POINTS TO FRONT");
        }
        StdOut.println();
    }

    /**
     * Prints the state of the classroom; can use this method to debug.
     */
    public void printClassroom() {
        printStudentsInLine();
        printSeatedStudents();
        printMusicalChairs();
    }

    /**
     * Used to get and set objects.
     */

    public SNode getStudentsInLine() { return studentsInLine; }
    public void setStudentsInLine(SNode l) { studentsInLine = l; }

    public SNode getMusicalChairs() { return musicalChairs; }
    public void setMusicalChairs(SNode m) { musicalChairs = m; }

    public boolean[][] getSeatingAvailability() { return seatingAvailability; }
    public void setSeatingAvailability(boolean[][] a) { seatingAvailability = a; }

    public Student[][] getStudentsSitting() { return studentsSitting; }
    public void setStudentsSitting(Student[][] s) { studentsSitting = s; }

}
