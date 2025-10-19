package oopchesscode.position;

public class Square {
private int rankIndex;
private int fileIndex;

public Square(int rankIndex, int fileIndex) {
this.rankIndex = rankIndex;
this.fileIndex = fileIndex;
}

public int getRankIndex() {
return rankIndex;
}

public int getFileIndex() {
return fileIndex;
}

@Override
public boolean equals(Object o) {
if (o instanceof Square) {
Square other = (Square) o;
return this.rankIndex == other.rankIndex && this.fileIndex == other.fileIndex;
}
return false;
}

@Override
public int hashCode() {
return rankIndex * 31 + fileIndex;
}

@Override
public String toString() {
return "(" + rankIndex + "," + fileIndex + ")";
}
}
