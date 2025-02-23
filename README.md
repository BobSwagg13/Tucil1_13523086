# **Puzzle Solver - Java Swing**

## **1. Program Overview**
Puzzle Solver is a **Java Swing**-based program designed to solve puzzles based on input from a text file. This program uses a **backtracking algorithm** to find solutions for the given puzzle. The solution will be displayed in a graphical user interface (GUI) and can be saved as a **.txt** file.

![IQ PUZZLER PRO](https://d32bxxnq6qs937.cloudfront.net/sites/default/files/smartgames_IQ_Puzzler_pro_STEP3.jpg)

---

## **2. Program Requirements and Installation**
### **2.1. Requirements**
- **Java Development Kit (JDK) 8 or later**
- **Java Swing** (already included in JDK)

### **2.2. Installation**
1. **Download and Install JDK** *(If not installed yet)*
   - Download JDK from [Oracle](https://www.oracle.com/java/technologies/javase-downloads.html) or [OpenJDK](https://openjdk.org/)
   - Add  **Java to PATH**
---

## **3. How to Compile the Program**
### **3.1. Open Terminal or Command Prompt**

```sh
javac -d bin -cp . *.java
```

This command compiles all Java files and saves them in the bin/ folder `bin/`.

---

## **4. How to Run and Use the Program**
### **4.1. Run the Program**
After compilation, run the program using the following command:

```sh
java -cp bin PuzzleSolverGUI
```

If using a package (`package src;`) at the top of the Java class), use the following command:

```sh
java -cp bin src.PuzzleSolverGUI
```

### **4.2. How to Use**
1. **Click `Upload File`** to select a puzzle text file.
2. **Click  `Solve Puzzle`** to run the solving algorithm.
3. **The solution will be displayed in the GUI.**
4. **Click `Save Solution`** to save the solution as a `.txt` file.
5. **The solution file will be automatically saved in the `test/solutions/` folder.**

---

## **5. Author / Creator Information**
- **Name:** Bob Kunanda
- **Email:** [bobkunanda@gmail.com](mailto:bobkunanda@gmail.com) 
- **NIM:** 13523086 





