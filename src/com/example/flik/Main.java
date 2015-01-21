package com.example.flik;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;

public class Main extends Activity{
	
	public DataFetcher myFetcher;
	public ArrayList<String> foodNames = new ArrayList<String>();
	public ArrayList<String> questions = new ArrayList<String>();
	
	public int surveyNumber = 1;
	public int numberLp = 0;
	
	public EditText etUsername;
	public String username = "Anonymous";
	public EditText[] etUserComment;
	public RatingBar[] ratingBar;
	public TextView[] tv;
	public int numberItems;
	
	//for onRatingChanged method
	public RatingBar ratingBarChanged;
	public float rating;
	public boolean fromUser;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ScrollView sv = new ScrollView(this);
		//setContentView(R.layout.activity_main);
		
		RelativeLayout relativeLayout = new RelativeLayout(this);
		
//BTW use "\n" to line break
		
		numberLp++;
		TextView tv1 = new TextView(this);
		tv1.setId(numberLp);
		tv1.setText("Welcome to the Flik Menu App!");
		tv1.setTextSize(40);
		LayoutParams lp1 = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);    
		lp1.addRule(RelativeLayout.CENTER_HORIZONTAL);
		lp1.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		tv1.setLayoutParams(lp1);
		relativeLayout.addView(tv1);
		
		numberLp++;
		TextView tv2 = new TextView(this);
		tv2.setId(numberLp);
		tv2.setText("Here is today's menu. Please take a moment to rate any item that you had today. \n");
		tv2.setTextSize(20);
		LayoutParams lp2 = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);    
		lp2.addRule(RelativeLayout.CENTER_HORIZONTAL);
		lp2.addRule(RelativeLayout.BELOW, numberLp-1);
		tv2.setLayoutParams(lp2);
		relativeLayout.addView(tv2);
		
		myFetcher = new DataFetcher();
		myFetcher.surveyNumber = surveyNumber;
		foodNames = myFetcher.getItemNames();
		numberItems = foodNames.size();
		System.out.println("numberItems = " + numberItems);

		ratingBar = new RatingBar[numberItems];
		LayoutParams[] lp = new LayoutParams[numberItems*2];
		tv = new TextView[numberItems];
	
		for(int i=0;i<numberItems;i++)
		{
			//Creating the textview for each item
			numberLp++;
			tv[i] = new TextView(this);
			tv[i].setId(numberLp);
			tv[i].setText(foodNames.get(i));
			tv[i].setTextSize(15);
			//tv[i].setTextSize(40);
			lp[i*2] = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);    
			lp[i*2].addRule(RelativeLayout.ALIGN_PARENT_LEFT);
			lp[i*2].addRule(RelativeLayout.BELOW, numberLp-1);
			tv[i].setLayoutParams(lp[i*2]);
			relativeLayout.addView(tv[i]);
			
			//Creating the rating bars for each item
			numberLp++;
			ratingBar[i] = new RatingBar(this);
			ratingBar[i].setId(numberLp);
			ratingBar[i].setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
				
				@Override
				public void onRatingChanged(RatingBar ratingBar, float rating,
						boolean fromUser) {
					// TODO Auto-generated method stub
					System.out.println("rating = " + rating);
					System.out.println("ratingBarNumber = " + ((ratingBar.getId()-4)/2));
					if(fromUser) {
						String tempFoodName = tv[(ratingBar.getId()-4)/2].getText().toString();
						int totalRating = myFetcher.getTotalRating(tempFoodName);
						int numberRating = myFetcher.getNumberRating(tempFoodName);
						System.out.println(myFetcher.updateRating(tempFoodName, (totalRating + (int)rating), (numberRating + 1)));
						ratingBar.setIsIndicator(true);
					}
				}
			});
			lp[i*2+1] = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
			lp[i*2+1].addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			lp[i*2+1].addRule(RelativeLayout.BELOW, numberLp-1);
			ratingBar[i].setLayoutParams(lp[i*2+1]);
			relativeLayout.addView(ratingBar[i]);
		}
		
		//Use a drawer?
		
		
		numberLp++;
		Button b1 = new Button(this);
		b1.setId(numberLp);
		b1.setTextSize(12);
		b1.setText("Show Results");
		b1.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
            	// Perform action on click
            	float coeff = 2f;
            	
            	for(int i=0;i<numberItems;i++)
         		{
            		int totalRating = myFetcher.getTotalRating(tv[i].getText().toString());
            		int numberRating = myFetcher.getNumberRating(tv[i].getText().toString());
            		System.out.println((float)Math.round((totalRating/numberRating)*2)/2);
            		ratingBar[i].setRating((float)Math.round((totalRating/numberRating)*2)/2);
         		}
            }
         });
		LayoutParams lpButton = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);    
		lpButton.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		lpButton.addRule(RelativeLayout.BELOW, numberLp-1);
		b1.setLayoutParams(lpButton);
		relativeLayout.addView(b1);
		
		numberLp++;
		TextView tv3 = new TextView(this);
		tv3.setId(numberLp);
		tv3.setText("Survey Questions:");
		tv3.setTextSize(20);
		LayoutParams lp3 = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		lp3.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		lp3.addRule(RelativeLayout.BELOW, numberLp-1);
		tv3.setLayoutParams(lp3);
		relativeLayout.addView(tv3);
		
		numberLp++;
		etUsername = new EditText(this);
		etUsername.setId(numberLp);
		etUsername.setHint("username");
		LayoutParams lp5 = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		lp5.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		lp5.addRule(RelativeLayout.BELOW, numberLp-1);
		etUsername.setLayoutParams(lp5);
		relativeLayout.addView(etUsername);
		
		int numberQuestions;
		questions = myFetcher.getQuestions();//Question number
		numberQuestions = questions.size();
		
		TextView[] tvQuestion = new TextView[numberQuestions];
		LayoutParams[] lpQuestion = new LayoutParams[numberQuestions];
		etUserComment = new EditText[numberQuestions];
		LayoutParams[] lpUserComment = new LayoutParams[numberQuestions];
		LayoutParams[] lpUserButton = new LayoutParams[numberQuestions];
		Button[] bUserComment = new Button[numberQuestions];
		
		for(int i=0;i<numberQuestions;i++)
		{
			//Creating the textview for each item
			numberLp++;
			tvQuestion[i] = new TextView(this);
			tvQuestion[i].setId(numberLp);
			tvQuestion[i].setText(questions.get(i));
			tvQuestion[i].setTextSize(15);
			lpQuestion[i] = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);    
			lpQuestion[i].addRule(RelativeLayout.ALIGN_PARENT_LEFT);
			lpQuestion[i].addRule(RelativeLayout.BELOW, numberLp-1);
			tvQuestion[i].setLayoutParams(lpQuestion[i]);
			relativeLayout.addView(tvQuestion[i]);
			
			int numberComments;
			ArrayList<String> comments = new ArrayList<String>();
			comments = myFetcher.getComments(i+1);//questionNumber
			numberComments = comments.size();
			ArrayList<String> commenters = new ArrayList<String>();
			commenters = myFetcher.getCommenterNames(i+1);//questionNumber
			
			LayoutParams[] lpComment = new LayoutParams[numberComments];
			TextView[] tvComment = new TextView[numberComments];
			
			for(int x=0;x<numberComments;x++)
			{
				numberLp++;
				tvComment[x] = new TextView(this);
				tvComment[x].setId(numberLp);
				tvComment[x].setText("\t\t"+commenters.get(x)+": "+comments.get(x));
				lpComment[x] = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);    
				lpComment[x].addRule(RelativeLayout.ALIGN_PARENT_LEFT);
				lpComment[x].addRule(RelativeLayout.BELOW, numberLp-1);
				tvComment[x].setLayoutParams(lpComment[x]);
				relativeLayout.addView(tvComment[x]);
			}
			
			numberLp++;
			etUserComment[i] = new EditText(this);
			etUserComment[i].setId(numberLp);
			//etUserComment[i].setTextSize(12);
			etUserComment[i].setHint("Comment here...");
			lpUserComment[i] = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
			lpUserComment[i].addRule(RelativeLayout.ALIGN_PARENT_LEFT);
			lpUserComment[i].addRule(RelativeLayout.BELOW, numberLp-1);
			etUserComment[i].setLayoutParams(lpUserComment[i]);
			relativeLayout.addView(etUserComment[i]);
			
			numberLp++;
			bUserComment[i] = new Button(this);
			final int buttonNumber = i;
			bUserComment[i].setId(numberLp);
			bUserComment[i].setTextSize(12);
			bUserComment[i].setText("Send");
			bUserComment[i].setOnClickListener(new View.OnClickListener() {
	             public void onClick(View v) {
	                 // Perform action on click
	            	 submitComment(buttonNumber);
	            	 reload();
	             }
	         });
			lpUserButton[i] = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);    
			lpUserButton[i].addRule(RelativeLayout.RIGHT_OF, numberLp-1);
			lpUserButton[i].addRule(RelativeLayout.BELOW, numberLp-2);
			bUserComment[i].setLayoutParams(lpUserButton[i]);
			relativeLayout.addView(bUserComment[i]);
		}
		
		sv.addView(relativeLayout);
		
		setContentView(sv);
		
		Intent intent = getIntent();
		if(intent.hasExtra("name") == true)
		{
			username = intent.getStringExtra("name");
			etUsername.setText(username);
		}
	}
	
	public void submitComment(int x) {
		
		System.out.println("button number = " + x);
		System.out.println("username = " + etUsername.getText().toString());
		System.out.println("etUserComment = " + etUserComment[x].getText().toString());
		
		if(etUsername.getText().length() == 0) {
			username = "Anonymous";
		}
		else {
			username = etUsername.getText().toString();
		}
		
		System.out.println("Comment submitted: " + myFetcher.submitComment(x, username, etUserComment[x].getText().toString()));
	}
	
	public void reload() {
		Intent intent = getIntent();
		if(username != "Anonymous") {
			intent.putExtra("name", username);
		}
		overridePendingTransition(0, 0);
	    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
	    finish();
	    
	    overridePendingTransition(0, 0);
	    startActivity(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	public void test(View view)
	{
		System.out.println("BUTTON DOWN");
	}
	
}
