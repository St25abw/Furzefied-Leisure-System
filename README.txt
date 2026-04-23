Furzefield Leisure Centre Booking System
7COM1025 - Programming for Software Engineers
University of Hertfordshire

==============================
PROJECT STRUCTURE
==============================
src/flc/          - Java source files
test/flc/         - JUnit 5 test files
FLC.jar           - Executable JAR (pre-compiled)
pom.xml           - Maven build file (requires internet for dependency download)
report/           - Design and implementation report (Word format)

==============================
HOW TO RUN
==============================
Option A - Run pre-compiled JAR:
  java -jar FLC.jar

Option B - Compile from source (Java 17+):
  javac -d out/classes $(find src -name "*.java")
  java -cp out/classes flc.Main

Option C - Maven build (requires Maven Central access):
  mvn package
  java -jar target/FLC.jar

==============================
HOW TO RUN TESTS
==============================
  Download JUnit 5 standalone jar from Maven Central:
    junit-platform-console-standalone-1.10.0.jar
  
  Compile tests:
    javac -cp out/classes:junit-platform-console-standalone.jar \
          -d out/test-classes test/flc/FLCTest.java
  
  Run tests:
    java -jar junit-platform-console-standalone.jar \
         --class-path out/classes:out/test-classes \
         --select-class flc.FLCTest

==============================
PRE-LOADED DATA
==============================
10 pre-registered members (IDs 1-10):
  1  Alice Johnson    2  Bob Smith       3  Carol White
  4  David Brown      5  Emma Davis      6  Frank Miller
  7  Grace Wilson     8  Harry Moore     9  Isla Taylor
  10 Jack Anderson

48 lessons across 8 weekends:
  Weekends 1-4 = April  (lessons L001-L024)
  Weekends 5-8 = May    (lessons L025-L048)
  
22+ attended bookings with reviews are pre-seeded so reports work immediately.

==============================
EXERCISE TYPES & PRICES
==============================
  Yoga       £12.00
  Zumba      £10.00
  Aquacise   £9.00
  Box Fit    £14.00
  Body Blitz £11.00
