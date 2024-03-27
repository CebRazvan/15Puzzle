package com.example.a15puzzle;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int GRID_SIZE = 4;
    private Button restartButton;
    private GridLayout gridLayout;
    private List<Button> buttons;
    private int[][] puzzle;
    private int emptyTileRow;
    private int emptyTileCol;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        restartButton = findViewById(R.id.restartButton);
        gridLayout = findViewById(R.id.gridLayout);
        buttons = new ArrayList<>();

        initializeGame();

        restartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initializeGame();
            }
        });
    }

    private void initializeGame() {
        gridLayout.removeAllViews();
        buttons.clear();

        // Initialize puzzle with numbers from 1 to 15
        puzzle = new int[GRID_SIZE][GRID_SIZE];
        int num = 1;
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                if (num == GRID_SIZE * GRID_SIZE) {
                    puzzle[i][j] = 0; // Empty tile
                    emptyTileRow = i;
                    emptyTileCol = j;
                } else {
                    puzzle[i][j] = num;
                    num++;
                }
            }
        }

        // Shuffle puzzle to create a solvable state
        shufflePuzzle();

        // Create buttons and add them to the grid layout
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                Button button = new Button(this);
                button.setLayoutParams(new GridLayout.LayoutParams());
                button.setText(String.valueOf(puzzle[i][j]));
                button.setOnClickListener(new PuzzleClickListener(i, j));
                buttons.add(button);
                button.setPadding(0, 0, 0, 0); // Remove padding
                if (puzzle[i][j] == 0) {
                    button.setBackgroundColor(Color.RED); // Set red color for 0
                }
                gridLayout.addView(button);
            }
        }
    }

    private void shufflePuzzle() {
        List<Integer> numbers = new ArrayList<>();
        for (int i = 1; i < GRID_SIZE * GRID_SIZE; i++) {
            numbers.add(i);
        }
        Collections.shuffle(numbers);

        int index = 0;
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                if (index < numbers.size()) {
                    puzzle[i][j] = numbers.get(index++);
                }
            }
        }
    }

    private boolean isSolved() {
        int num = 1;
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                if (puzzle[i][j] != num % (GRID_SIZE * GRID_SIZE)) {
                    return false;
                }
                num++;
            }
        }
        return true;
    }

    private class PuzzleClickListener implements View.OnClickListener {
        private int row;
        private int col;

        PuzzleClickListener(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public void onClick(View v) {
            if (isValidMove(row, col)) {
                swapTiles(row, col);
                if (isSolved()) {
                    Toast.makeText(MainActivity.this, "Congratulations! You solved the puzzle!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private boolean isValidMove(int row, int col) {
        return (row == emptyTileRow && Math.abs(col - emptyTileCol) == 1) ||
                (col == emptyTileCol && Math.abs(row - emptyTileRow) == 1);
    }

    private void swapTiles(int row, int col) {
        int temp = puzzle[row][col];
        puzzle[row][col] = puzzle[emptyTileRow][emptyTileCol];
        puzzle[emptyTileRow][emptyTileCol] = temp;

        // Update empty tile position
        emptyTileRow = row;
        emptyTileCol = col;

        // Update button texts
        for (int i = 0; i < buttons.size(); i++) {
            Button button = buttons.get(i);
            int buttonRow = i / GRID_SIZE;
            int buttonCol = i % GRID_SIZE;
            button.setText(String.valueOf(puzzle[buttonRow][buttonCol]));
            if (puzzle[buttonRow][buttonCol] == 0) {
                button.setBackgroundColor(Color.RED); // Set red color for 0
            } else {
                button.setBackgroundColor(Color.WHITE); // Reset color for other buttons
            }
        }
    }
}
