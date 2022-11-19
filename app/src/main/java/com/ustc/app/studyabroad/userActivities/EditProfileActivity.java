package com.ustc.app.studyabroad.userActivities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.hbb20.CountryCodePicker;
import com.ustc.app.studyabroad.FirebaseStorageHelper;
import com.ustc.app.studyabroad.Helper;
import com.ustc.app.studyabroad.R;
import com.ustc.app.studyabroad.ToastDisplay;
import com.ustc.app.studyabroad.activities.GetSearchableUniversities;
import com.ustc.app.studyabroad.interfaces.Callback;
import com.ustc.app.studyabroad.interfaces.GetProfileCallback;
import com.ustc.app.studyabroad.interfaces.ReturnCallback;
import com.ustc.app.studyabroad.userModels.Profile;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import de.hdodenhof.circleimageview.CircleImageView;
//aboye0123@gmail.com
//field.clearFocus();
//field.setErrorEnabled(true);
//field.setError(getString(ex.reasonRes));
public class EditProfileActivity extends AppCompatActivity {

    Helper helper = new Helper();
    private final int PICK_IMAGE_REQUEST = 22;
    private static final int CODE = 234;
    private Uri imagePath;
    private Uri filePath;
    DatePickerDialog datePickerDialog;

    CountryCodePicker code;
    ImageView im;
    CircleImageView profile_img;
    AutoCompleteTextView gender, degree_level, edu_country, cgpa_outOf, research_papers, interested_term, interested_year, test_type, language_test_type;
    EditText u_name, email, f_name, l_name, p_num, address, website, work_exp_total, inst_name, cgpa, target_major;
    TextView country, target_uni_name;
    EditText about, birthday;
    ImageView search_target_uni_name;
    TextView change_resume, finish_btn, resume_filename;

    LinearLayout gmat_score, sat_score, toefl_score, gre_score, ielts_score, act_score;
    EditText s_math, s_reading_writing, a_math, a_english, a_reading, a_science, g_verbal, g_quantitative, g_writing, gmat_verbal,
            gmat_quantitative, gmat_writing, gmat_reasoning;
    EditText t_reading, t_speaking, t_listening, t_writing, i_reading, i_speaking, i_listening, i_writing;

    private FirebaseAuth mAuth;
    private FirebaseUser user;

    String[] genderItems, degree_level_items, cgpa_items, research_papers_items, work_exp_items, interested_term_items,
            interested_year_items, test_types_items, language_test_types_items, edu_country_items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        setView();
        //profile_img.setVisibility(View.INVISIBLE);
        getData();
        back();
        uploadResume();
        //setProfileImg();
        hideLayouts();
        defineStringArrays();
        //setBirthday();
        helper.setDate(birthday, EditProfileActivity.this);
        setUser();
        setCountry();
        setAddress();
        searchTargetUni();
        //setAutoCompleteTextView();
        done();

    }

    private void getData() {
        ProgressDialog progressDialog = Helper.createProgressDialog(EditProfileActivity.this);
        progressDialog.show();
        FirebaseStorageHelper.getProfile(FirebaseStorageHelper.getUserId(), new GetProfileCallback() {
            @Override
            public void onSuccess(Profile profile) {
                setData(profile);
                progressDialog.dismiss();
                setAutoCompleteTextView();
            }
            @Override
            public void onFailure(String er) {
                if(er.equals("f")) {
                    ToastDisplay.customMsg(getApplicationContext(), "Data was unable to be fetched.");
                } else{
                    profile_img.setVisibility(View.VISIBLE);
                }
                setAutoCompleteTextView();
                progressDialog.dismiss();
            }
        });
    }

    private void searchTargetUni() {
        search_target_uni_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditProfileActivity.this, GetSearchableUniversities.class);
                startActivityForResult(intent, 2);
            }
        });
    }

    private void setAutoCompleteTextView() {
        helper.getItem(genderItems, this, gender);
        helper.getItem(degree_level_items, this, degree_level);
        helper.getItem(cgpa_items, this, cgpa_outOf);
        helper.getItem(research_papers_items, this, research_papers);
        helper.getItem(interested_term_items, this, interested_term);
        helper.getItem(interested_year_items, this, interested_year);
        helper.getItem(edu_country_items, this, edu_country);
        helper.getItemResponse(test_types_items, this, test_type, new ReturnCallback(){
            @Override
            public void onSuccess(String selectedItem) {
                displayTestTypes(selectedItem);
            }
            @Override
            public void onFailure(String val) { }
        });
        helper.getItemResponse(language_test_types_items, this, language_test_type, new ReturnCallback(){
            @Override
            public void onSuccess(String selectedItem) {
                displayLanguageTestTypes(selectedItem);
            }

            @Override
            public void onFailure(String val) { }

        });
    }

    private void back() {
        im.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void uploadResume() {
        change_resume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
                intent.setType("*/*");
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                startActivityForResult(intent, CODE);
            }
        });
    }

    private void setProfileImg() {
        profile_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Defining Implicit Intent to mobile gallery
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Image from here..."), PICK_IMAGE_REQUEST);
            }
        });
    }

    private void done() {
        finish_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgressDialog progressDialog = Helper.createProgressDialog(EditProfileActivity.this);
                progressDialog.show();
                String p_number;
                if(p_num.getText().toString().contains("+")){
                    p_number=p_num.getText().toString();
                }
                else{
                    p_number="+"+code.getSelectedCountryCode()+p_num.getText().toString();
                }
                try{
                    Profile profile = new Profile(user.getUid(), user.getDisplayName(), user.getEmail(), f_name.getText().toString(), l_name.getText().toString(), p_number, country.getText().toString(), address.getText().toString(), about.getText().toString(), website.getText().toString(),
                            gender.getText().toString(), birthday.getText().toString(), degree_level.getText().toString(), edu_country.getText().toString(), inst_name.getText().toString(), cgpa.getText().toString() + "/" + cgpa_outOf.getText().toString(), research_papers.getText().toString(),
                            work_exp_total.getText().toString(), target_uni_name.getText().toString(), target_major.getText().toString(), interested_term.getText().toString(), interested_year.getText().toString(), test_type.getText().toString(), getTestScore(test_type.getText().toString()),
                            language_test_type.getText().toString(), getLanguageTestScore(language_test_type.getText().toString()));
                    FirebaseStorageHelper.saveProfile(profile);
                    progressDialog.dismiss();
                }catch (Exception e){
                    progressDialog.dismiss();
                    ToastDisplay.customMsg(getApplicationContext(), "Saving failed. Please try again.");
                }
            }
        });
    }

    private Boolean showDialogMeg() {
        new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Saving failed!").setMessage("Would you like to try again or go back?")
                .setPositiveButton("Try again", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //set what would happen when positive button is clicked
                        dialogInterface.dismiss();
                    }
                }).setNegativeButton("Go back", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Toast.makeText(getApplicationContext(),"Nothing Happened",Toast.LENGTH_LONG).show();
                    }
                }).show();
        return true;
    }

    private void setAddress() {
        Places.initialize(getApplicationContext(), "AIzaSyCLSUpToPothqV-x14KzrmLBewtStdb5Pk");
        address.setFocusable(false);
        address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Place.Field> fieldList = Arrays.asList(Place.Field.ADDRESS, Place.Field.LAT_LNG, Place.Field.NAME);
                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fieldList).build(EditProfileActivity.this);
                startActivityForResult(intent, 100);
            }
        });
    }

    private void setCountry() {
        code.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                country.setText(code.getSelectedCountryName());
            }
        });
    }

    private void setUser() {
        if(user != null) {
            u_name.setText(user.getDisplayName());
            email.setText(user.getEmail());
            u_name.setEnabled(false);
            email.setEnabled(false);
            //String id = mDatabase.push().getKey();
            System.out.println("id >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> " + user.getUid());
        }
    }

    private void displayLanguageTestTypes(String selectedItem) {
        switch (selectedItem) {
            case "TOEFL":
                hideLayouts();
                toefl_score.setVisibility(View.VISIBLE);
                break;
            case "IELTS":
                hideLayouts();
                ielts_score.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }

    private void displayTestTypes(String selected) {
        switch(selected){
            case "SAT":
                hideLayouts();
                sat_score.setVisibility(View.VISIBLE);
                break;
            case "ACT":
                hideLayouts();
                act_score.setVisibility(View.VISIBLE);
                break;
            case "GRE":
                hideLayouts();
                gre_score.setVisibility(View.VISIBLE);
                break;
            case "GMAT":
                hideLayouts();
                gmat_score.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }

    private void defineStringArrays() {
        genderItems = getResources().getStringArray(R.array.gender);
        degree_level_items = getResources().getStringArray(R.array.degree_level);
        cgpa_items = getResources().getStringArray(R.array.cgpa);
        research_papers_items = getResources().getStringArray(R.array.research_papers);
        work_exp_items = getResources().getStringArray(R.array.work_exp);
        interested_term_items = getResources().getStringArray(R.array.interested_term);
        interested_year_items = getResources().getStringArray(R.array.interested_year);
        test_types_items = getResources().getStringArray(R.array.test_types);
        language_test_types_items = getResources().getStringArray(R.array.language_test_types);
        edu_country_items = getResources().getStringArray(R.array.all_countries);
    }

    private void hideLayouts() {
        sat_score.setVisibility(View.GONE);
        act_score.setVisibility(View.GONE);
        gre_score.setVisibility(View.GONE);
        gmat_score.setVisibility(View.GONE);
        toefl_score.setVisibility(View.GONE);
        ielts_score.setVisibility(View.GONE);
    }

    private String getLanguageTestScore(String language_test_type) {
        switch (language_test_type) {
            case "TOEFL":
                return("Reading: " + t_reading.getText().toString() + "\nWriting: " + t_writing.getText().toString() + "\nListening: " + t_listening.getText().toString() + "\nSpeaking: " + t_speaking.getText().toString());
            case "IELTS":
                return("Reading: " + i_reading.getText().toString() + "\nWriting: " + i_writing.getText().toString() + "\nListening: " + i_listening.getText().toString() + "\nSpeaking: " + i_speaking.getText().toString());
            default:
                return null;
        }
    }

    private String getTestScore(String test_type) {
        switch (test_type) {
            case "SAT":
                return ("Math: " + s_math.getText().toString() + "\nReading and Writing: " + s_reading_writing.getText().toString());
            case "ACT":
                return ("Math: " + a_math.getText().toString() + "\nEnglish: " + a_english.getText().toString() + "\nReading: " + a_reading.getText().toString() + "\nScience: " + a_science.getText().toString());
            case "GRE":
                return ("Verbal: " + g_verbal.getText().toString() + "\nQuantitative: " + g_quantitative.getText().toString() + "\nWriting: " + g_writing.getText().toString());
            case "GMAT":
                return ("Verbal: " + gmat_verbal.getText().toString() + "\nQuantitative: " + gmat_quantitative.getText().toString() + "\nAnalytical Writing: " + gmat_writing.getText().toString() + "\nIntegrated Reasoning: " + gmat_reasoning.getText().toString());
            default:
                return null;
        }
    }

    private void setTestScore(String test_type, String val) {
        if(val!=null){
            String[] scores = val.split("\n");
            switch (test_type) {
                case "SAT":
                    s_math.setText(scores[0].split(":")[1]);
                    s_reading_writing.setText(scores[1].split(":")[1]);
                    break;
                case "ACT":
                    a_math.setText(scores[0].split(":")[1]);
                    a_english.setText(scores[1].split(":")[1]);
                    a_reading.setText(scores[2].split(":")[1]);
                    a_science.setText(scores[3].split(":")[1]);
                    break;
                case "GRE":
                    g_verbal.setText(scores[0].split(":")[1]);
                    g_quantitative.setText(scores[1].split(":")[1]);
                    g_writing.setText(scores[2].split(":")[1]);
                    break;
                case "GMAT":
                    gmat_verbal.setText(scores[0].split(":")[1]);
                    gmat_quantitative.setText(scores[1].split(":")[1]);
                    gmat_writing.setText(scores[2].split(":")[1]);
                    gmat_reasoning.setText(scores[3].split(":")[1]);
                default:
                    break;
            }
        }
    }
    private void setLanguageTestScore(String test_type, String val) {
        if(val!=null) {
            String[] scores = val.split("\n");
            switch (test_type) {
                case "TOEFL":
                    t_reading.setText(scores[0].split(":")[1]);
                    t_writing.setText(scores[1].split(":")[1]);
                    t_listening.setText(scores[2].split(":")[1]);
                    t_speaking.setText(scores[3].split(":")[1]);
                    break;
                case "IELTS":
                    i_reading.setText(scores[0].split(":")[1]);
                    i_writing.setText(scores[1].split(":")[1]);
                    i_listening.setText(scores[2].split(":")[1]);
                    i_speaking.setText(scores[3].split(":")[1]);
                    break;
                default:
                    break;
            }
        }
    }

    private void setView() {
        im = (ImageView) findViewById(R.id.back);
        profile_img = (CircleImageView) findViewById(R.id.profile_img);
        u_name = (EditText) findViewById(R.id.u_name);
        email = (EditText) findViewById(R.id.email);
        change_resume = (TextView) findViewById(R.id.change_resume);

        gmat_score = (LinearLayout) findViewById(R.id.gmat_score);
        sat_score = (LinearLayout) findViewById(R.id.sat_score);
        toefl_score = (LinearLayout) findViewById(R.id.toefl_score);
        gre_score = (LinearLayout) findViewById(R.id.gre_score);
        act_score = (LinearLayout) findViewById(R.id.act_score);
        ielts_score = (LinearLayout) findViewById(R.id.ielts_score);

        profile_img = findViewById(R.id.profile_img);
        f_name = findViewById(R.id.f_name);
        l_name = findViewById(R.id.l_name);
        code = findViewById(R.id.code);
        p_num = findViewById(R.id.p_num);
        website = findViewById(R.id.website);
        work_exp_total = findViewById(R.id.work_exp_total);
        inst_name = findViewById(R.id.inst_name);
        cgpa = findViewById(R.id.cgpa);
        research_papers = findViewById(R.id.research_papers);
        country = findViewById(R.id.country);
        address = findViewById(R.id.address);
        gender = findViewById(R.id.gender);
        birthday = findViewById(R.id.birthday);
        edu_country = findViewById(R.id.edu_country);
        degree_level = findViewById(R.id.degree_level);
        inst_name = findViewById(R.id.inst_name);
        cgpa_outOf = findViewById(R.id.cgpa_outOf);
        target_uni_name = findViewById(R.id.target_uni_name);
        target_major = findViewById(R.id.target_major);
        interested_term = findViewById(R.id.interested_term);
        interested_year = findViewById(R.id.interested_year);
        test_type = findViewById(R.id.test_type);
        language_test_type = findViewById(R.id.language_test_type);
        about = findViewById(R.id.about);
        search_target_uni_name = findViewById(R.id.search_target_uni_name);
        change_resume = findViewById(R.id.change_resume);
        finish_btn = findViewById(R.id.finish_btn);
        resume_filename = findViewById(R.id.resume_filename);

        s_math = findViewById(R.id.s_math);
        s_reading_writing = findViewById(R.id.s_reading_writing);
        a_english = findViewById(R.id.a_english);
        a_math = findViewById(R.id.a_math);
        a_reading = findViewById(R.id.a_reading);
        a_science = findViewById(R.id.a_science);
        g_quantitative = findViewById(R.id.g_quantitative);
        g_verbal = findViewById(R.id.g_verbal);
        g_writing = findViewById(R.id.g_writing);
        gmat_quantitative = findViewById(R.id.gmat_quantitative);
        gmat_reasoning = findViewById(R.id.gmat_reasoning);
        gmat_verbal = findViewById(R.id.gmat_verbal);
        gmat_reasoning = findViewById(R.id.gmat_reasoning);
        t_listening = findViewById(R.id.t_listening);
        t_reading = findViewById(R.id.t_reading);
        t_speaking = findViewById(R.id.t_speaking);
        t_writing = findViewById(R.id.t_writing);
        i_listening = findViewById(R.id.i_listening);
        i_reading = findViewById(R.id.i_reading);
        i_speaking = findViewById(R.id.i_speaking);
        i_writing = findViewById(R.id.i_writing);
    }

    private void setData(Profile profile) {
        if(profile.getPhoneNumber() != ""){
            code.setVisibility(View.GONE);
            p_num.setText(profile.getPhoneNumber());
            p_num.setEnabled(false);
        }

        //setImage();
        String[] c = profile.getCgpa().split("/", 2);

        f_name.setText(profile.getFirstName());
        l_name.setText(profile.getLastName());
        website.setText(profile.getWebsite());
        work_exp_total.setText(profile.getWork_exp());
        cgpa.setText(c[0]);
        research_papers.setText(profile.getResearch_paper());
        country.setText(profile.getCountry());
        address.setText(profile.getAddress());
        gender.setText(profile.getGender());
        birthday.setText(profile.getBirthday());
        edu_country.setText(profile.getEdu_country());
        degree_level.setText(profile.getDegree_level());
        inst_name.setText(profile.getEdu_place_name());
        cgpa_outOf.setText(c[1]);
        target_uni_name.setText(profile.getTarget_uni_name());
        target_major.setText(profile.getTarget_major());
        interested_term.setText(profile.getInterested_term());
        interested_year.setText(profile.getInterested_year());
        test_type.setText(profile.getTest_type());
        language_test_type.setText(profile.getLanguage_test_type());
        about.setText(profile.getAbout());
        //filePath = Uri.parse(profile.getResume());
        /*if(profile.getResume()!=""){
            //setResumeFilename(profile.getResume());
            Helper.setResumeFilename(profile.getResume(), resume_filename);
        }*/
        setTestScore(profile.getTest_type(), profile.getTest_score());
        setLanguageTestScore(profile.getLanguage_test_type(), profile.getLanguage_test_score());
    }

    private void setImage() {
        FirebaseStorageHelper.setImage(getApplicationContext(), profile_img, user.getUid(), new Callback() {
            @Override
            public void onSuccess() {
                profile_img.setVisibility(View.VISIBLE);
            }
            @Override
            public void onFailure() {
                profile_img.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            // Get the Uri of data
            imagePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imagePath);
                profile_img.setImageBitmap(bitmap);
                ToastDisplay.customMsgShort(getApplicationContext(), "Image uploaded");
                //System.out.println("IMAGE FILEPATH >>>>>>>>>>>>> " + imagePath);
            }
            catch (IOException e) {
                // Log the exception
                e.printStackTrace();
            }
        }
        if (requestCode == CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            String fileName = Helper.getNameFromURI(getApplicationContext(), filePath);
            resume_filename.setText(fileName);
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                System.out.println("FILE FILEPATH >>>>>>>>>>>>> " + filePath);
                //imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (resultCode == 123 && requestCode == 2) {
            String returnData = data.getStringExtra("value");
            target_uni_name.setText(returnData);
        }
        if (requestCode == 100 && resultCode == RESULT_OK) {
            Place place = Autocomplete.getPlaceFromIntent(data);
            address.setText(place.getAddress());
        }
        if(resultCode == AutocompleteActivity.RESULT_ERROR){
            Status status = Autocomplete.getStatusFromIntent(data);
            ToastDisplay.customMsg(getApplicationContext(), status.getStatusMessage());
        }
    }
}
