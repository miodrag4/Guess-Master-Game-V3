package com.example.guessmaster;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import java.util.Random;

public class GuessMasterActivity extends AppCompatActivity {

    //Layout Setup
    private TextView entityName;
    private TextView ticketsum;
    private Button guessButton;
    private EditText userIn;
    private Button btnclearContent;
    // Code would not work with this in, the assignment asked for it but i am not sure why.
    // String user input;
    private ImageView entityImage;
    String answer;

    //Entity Info and Parameters
    private int entityId;
    private String entName;
    private final Entity[] entities;
    private Entity currentEntity;
    private int numOfEntities;
    private int TotalTickets = 0;
    private int TicketsWon = 0;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Initialzies the Layout
        setContentView(R.layout.activity_guess_activity);
        ticketsum = (TextView) findViewById(R.id.ticket);
        guessButton = (Button) findViewById(R.id.btnGuess);
        userIn = (EditText) findViewById(R.id.guessinput);
        btnclearContent = (Button) findViewById(R.id.btnClear);
        entityName = (TextView) findViewById(R.id.entityName);
        entityImage = (ImageView) findViewById(R.id.entityImage);

        //Defines Entities
        Country usa = new Country("United States", new Date("July", 4, 1776), "Washington D.C.", 0.1);
        Person myCreator = new Person("My Creator", new Date("September", 1, 2000), "Male", 1);
        Politician jTrudeau = new Politician("Justin Trudeau", new Date("December", 25, 1971), "Male", "Liberal", 0.25);
        Singer cDion = new Singer("Celine Dion", new Date("March", 30, 1961), "Female", "La voix du bon Dieu", new Date("November", 6, 1981), 0.5);

        new GuessMasterActivity();

        //Adds Entities
        addEntity(usa);
        addEntity(myCreator);
        addEntity(jTrudeau);
        addEntity(cDion);
        changeEntity();

        //Game Start
        welcomeToGame(currentEntity);

        btnclearContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeEntity();
            }
        });

        guessButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playGame(currentEntity);
            }
        });
    }

    public void changeEntity() {
        userIn.getText().clear();
        entityId = genRandomentityId();
        Entity entity = entities[entityId];
        entName = entity.getName();
        entityName.setText(entName);
        ImageSetter();
        currentEntity = entity;
    }

    public void ImageSetter() {
        String name = entName;
        switch (name) {
            case "Justin Trudeau":
                entityImage.setImageResource(R.drawable.justint);
                break;
            case "United States":
                entityImage.setImageResource(R.drawable.usaflag);
                break;
            case "Celine Dion":
                entityImage.setImageResource(R.drawable.celidion);
                break;
            case "My Creator":
                entityImage.setImageResource(R.drawable.download);
                break;    
        }
    }
    
    public void welcomeToGame(@NonNull Entity entity) {
        //Welcome Alert
        AlertDialog.Builder welcomealert = new AlertDialog.Builder(GuessMasterActivity.this);
        // System . o u t . p r i n t l n ( ” (mm/ dd / yyyy ) ” ) ;
        welcomealert.setTitle("GuessMaster_Game_v3");
        welcomealert.setMessage(entity.welcomeMessage());
        welcomealert.setCancelable(false);
        welcomealert.setNegativeButton("START_GAME", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getBaseContext(), "Game_is_Starting...Enjoy", Toast.LENGTH_SHORT).show();
            }
        });
        //Show Dialog
        AlertDialog dialog = welcomealert.create();
        dialog.show ();
    }

    public GuessMasterActivity() {
        numOfEntities = 0;
        entities = new Entity[10];
        TotalTickets = 0;
    }

    public void addEntity(Entity entity) {
        entities[numOfEntities++] = new Entity(entity) {
            public String entityType() {
                return null;
            }
            public Entity clone() {
                return null;
            }
        };
    }

    public void playGame(int entityId) {
        Entity entity = entities[entityId];
        playGame(entity);
    }

    // Main Game Function
    public void playGame(Entity entity) {

        //Name of the entity to be guessed in the entityName textview
        entityName.setText(entity.getName());
        //Get input from the EdiText
        answer = userIn.getText().toString();
        answer = answer.replace("\n", "").replace("\r","");
        Date date = new Date(answer);

        if (date.precedes(entity.getBorn())) {
            AlertDialog.Builder alert = new AlertDialog.Builder(GuessMasterActivity.this);
            alert.setTitle("Incorrect");
            alert.setMessage("Try an Later Date.");
            alert.setNegativeButton("Ok", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
            alert.show();
        } else if (entity.getBorn().precedes(date)) {
            AlertDialog.Builder alert = new AlertDialog.Builder(GuessMasterActivity.this);
            alert.setTitle("Incorrect");
            alert.setMessage("Try an Earlier Date.");
            alert.setNegativeButton("Ok", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
            alert.show();
        } else {
            TicketsWon = entity.getAwardedTicketNumber();
            TotalTickets += TicketsWon;
            String total = (new Integer(TotalTickets)).toString();

            AlertDialog.Builder alert = new AlertDialog.Builder(GuessMasterActivity.this);
            alert.setTitle("You WON!");
            alert.setMessage("BINGO!! \n" + entity.closingMessage());
            alert.setCancelable(false);
            alert.setNegativeButton("Continue", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialogInterface, int i) {
                    Toast.makeText(getBaseContext(), "You got" + total, Toast.LENGTH_SHORT).show();
                    continueGame();
                }
            });
            ticketsum.setText("Total Tickets" + total);
            alert.show();
        }
    }

    //Continue game, Play game and, Random name generator method and game functions
    public void continueGame(){
        userIn.getText().clear();
        changeEntity();
    }
    public void playGame() {
        int entityId = genRandomentityId();
        playGame(entityId);
    }
    public int genRandomentityId() {
        Random randomNumber = new Random();
        return randomNumber.nextInt(numOfEntities);
    }
}