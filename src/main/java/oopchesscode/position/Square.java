package oopchesscode.position;

import java.util.Objects;

public class Square {

    private final int rankIndex;
    private final int fileIndex;

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
        if (this == o) {
            return true;
        }
        if (!(o instanceof Square)) {
            return false;
        }
        Square other = (Square) o;
        return rankIndex == other.rankIndex && fileIndex == other.fileIndex;
    }

    @Override
    public int hashCode() {
        return Objects.hash(rankIndex, fileIndex);
    }

    @Override
    public String toString() {
        return "(" + rankIndex + "," + fileIndex + ")";
    }
}
