package com.myapp.tremplist_update.fireBase;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.myapp.tremplist_update.UI.DriverFirstPage;
import com.myapp.tremplist_update.R;
import com.myapp.tremplist_update.model.Ride;
import com.myapp.tremplist_update.model.User;

import java.util.Objects;

public class FireBaseDBActivity extends FirebaseBaseModel {
    String token_to_send_to = "";
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    Context context;
    Context ApplicationContext;
    Activity activity;

    public void setContext(Context context) {
        this.context = context;
    }

    public void addUserToDB(User user) {
        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(
                new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (task.isSuccessful()) {
                            token_to_send_to = Objects.requireNonNull(task.getResult()).getToken();
                            myRef.child("tokens").child(user.id).setValue(token_to_send_to);
                        }

                    }
                });
        myRef.child("users").child(user.id).setValue(user);

    }

    public void addRideToDB(Ride ride) {
        // getting the UID of the user from the Authentication
        String UID = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        myRef.child("users").child(UID).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                } else {
                    // Adding the driver to the ride
                    User driver = Objects.requireNonNull(task.getResult()).getValue(User.class);
                    ride.setDriver(driver);
                    myRef.child("rides").push().setValue(ride).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(context, "הנסיעה פורסמה בהצלחה!", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(context, "חלה שגיאה במהלך הפרסום" + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }

        });

    }

    public void updateRideOnDB_join(Ride curr_ride) {

        String trempist_UID = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        myRef.child("users").child(trempist_UID).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                } else {
                    // Adding the driver to the ride
                    User Trempist = Objects.requireNonNull(task.getResult()).getValue(User.class);
                    if (!curr_ride.add_to_waiting_list(Trempist)) {
                        Toast.makeText(context, "כבר שלחת בקשה להצטרף לנסיעה זו", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        myRef.child("rides").child(curr_ride.getId()).setValue(curr_ride).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(context, "הבקשה שלך נשלחה לנהג בהצלחה", Toast.LENGTH_SHORT).show();

                                    String driver_UID = curr_ride.getDriver().id;
                                    myRef.child("tokens").child(driver_UID).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                                            if (!task.isSuccessful()) {
                                                Log.e("firebase", "Error getting data", task.getException());
                                            } else {
                                                // Adding the driver to the ride
                                                String TOKEN = Objects.requireNonNull(task.getResult()).getValue(String.class);
                                                FcmNotificationsSender notificationsSender = new FcmNotificationsSender(
                                                        TOKEN,
                                                        "מצטרפ/ת חדש/ה!",
                                                        Trempist.getFirst_name() + " " + Trempist.getLast_name() + " מעוניינ/ת להצטרף לנסיעה שלך",
                                                        ApplicationContext, activity);
                                                notificationsSender.SendNotifications();

                                            }
                                        }

                                    });


                                } else {
                                    Toast.makeText(context, "Error: " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }
            }
        });


    }


    public void updateRideOnDB_Cancel(Ride curr_ride) {

        String trempist_UID = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        myRef.child("users").child(trempist_UID).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                } else {
                    // Adding the driver to the ride
                    User Trempist = Objects.requireNonNull(task.getResult()).getValue(User.class);
                    curr_ride.remove_from_Tremplists(Trempist);
                    myRef.child("rides").child(curr_ride.getId()).setValue(curr_ride).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(context, "הנסיעה בוטלה", Toast.LENGTH_SHORT).show();
                                String driver_UID = curr_ride.getDriver().id;
                                myRef.child("tokens").child(driver_UID).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                                        if (!task.isSuccessful()) {
                                            Log.e("firebase", "Error getting data", task.getException());
                                        } else {
                                            // Adding the driver to the ride
                                            String TOKEN = Objects.requireNonNull(task.getResult()).getValue(String.class);
                                            FcmNotificationsSender notificationsSender = new FcmNotificationsSender(
                                                    TOKEN,
                                                    "ביטול הצטרפות",
                                                    Trempist.getFirst_name() + " " + Trempist.getLast_name() + " ביטל את ההצטרפות לנסיעה שלך",
                                                    ApplicationContext, activity);
                                            notificationsSender.SendNotifications();

                                        }
                                    }

                                });


                            } else {
                                Toast.makeText(context, "Error: " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });


    }


    public void Cancel_by_Driver(Ride curr_ride) {
        String driver_UID = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        myRef.child("rides").child(curr_ride.getId()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                for (String trempist_id : curr_ride.getTrempists().keySet()) {
                    myRef.child("tokens").child(trempist_id).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            if (!task.isSuccessful()) {
                                Log.e("firebase", "Error getting data", task.getException());
                            } else {
                                // Adding the driver to the ride
                                String TOKEN = Objects.requireNonNull(task.getResult()).getValue(String.class);
                                FcmNotificationsSender notificationsSender = new FcmNotificationsSender(
                                        TOKEN,
                                        "ביטול נסיעה",
                                        "): " + curr_ride.getDriver().getFirst_name() + " " + curr_ride.getDriver().getLast_name() + " ביטל את הנסיעה",
                                        ApplicationContext, activity);
                                notificationsSender.SendNotifications();

                            }
                        }

                    });
                }
            }
        });

    }

    public void updateRate(Ride ride,int rate, String uid_passenger){
        myRef.child("users").child(uid_passenger).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                } else {
                    User passenger = Objects.requireNonNull(task.getResult()).getValue(User.class);
                    ride.add_rated(passenger);
                    myRef.child("rides").child(ride.getId()).setValue(ride);

                    myRef.child("users").child(ride.getDriver().getId()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            User driver = task.getResult().getValue(User.class);
                            assert driver != null;
                            driver.setCount_rate(driver.getCount_rate()+1);
                            driver.setSum_rate(driver.getSum_rate()+rate);
                            myRef.child("users").child(ride.getDriver().getId()).setValue(driver);
                        }
                    });
                }
            }
        }) ;
            }
    public void update_relevant_driver_details(User user_to_update, String UID) {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("rides");

        reference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                } else {
                    for (DataSnapshot rides : Objects.requireNonNull(task.getResult()).getChildren()) {
                        for (DataSnapshot trempists : rides.getChildren()) {
                            for (DataSnapshot tremp_user : trempists.getChildren()) {

                                // Update driver details inside a ride
                                Ride ride = rides.getValue(Ride.class);
                                assert ride != null;
                                User driver_to_change = ride.getDriver();
                                String uid = driver_to_change.getId();
                                if (uid.equals(UID)) {
                                    String ride_id = rides.getKey();
                                    assert ride_id != null;
                                    reference.child(ride_id).child("driver").setValue(user_to_update);
                                }

                                // update a trempitss details inside a ride
                                String user_id = tremp_user.getKey();
                                System.out.println(user_id);
                                assert user_id != null;
                                if (user_id.equals(UID)) {
                                    reference.child(Objects.requireNonNull(rides.getKey())).child("trempists")
                                            .child(user_id).setValue(user_to_update);
                                }
                            }
                        }
                    }
                }
            }
        });

    }

    public void showRate(TextView rate) {
        String UID = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        myRef.child("users").child(UID).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                } else {
                    // Adding the driver to the ride
                    User driver = Objects.requireNonNull(task.getResult()).getValue(User.class);
                    try {
                        if (driver.getCount_rate() > 0) {
                            int totalRate = Math.round(driver.getSum_rate() / driver.getCount_rate());
                            rate.setText(totalRate+"/5");
                        }
                    }catch(Exception exc){
                        Log.e("Old account", "Error getting data - need to re-register", exc);
                    }
                }
            }
        });
    }

    public void approve_joining(Ride curr_ride, User curr_trempist) {
        myRef.child("rides").child(curr_ride.getId()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                } else {
                    // Adding the driver to the ride
//                    Ride ride = Objects.requireNonNull(task.getResult()).getValue(Ride.class);
                    curr_ride.add_to_trempists(curr_trempist);
                    curr_ride.remove_from_waitingList(curr_trempist);

                    myRef.child("rides").child(curr_ride.getId()).setValue(curr_ride).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                myRef.child("tokens").child(curr_trempist.getId()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                                        if (!task.isSuccessful()) {
                                            Log.e("firebase", "Error getting data", task.getException());
                                        } else {
                                            // Adding the driver to the ride
                                            String TOKEN = Objects.requireNonNull(task.getResult()).getValue(String.class);
                                            FcmNotificationsSender notificationsSender = new FcmNotificationsSender(
                                                    TOKEN,
                                                    "אישור הצטרפות!",
                                                    curr_ride.getDriver().getFirst_name() + " " + curr_ride.getDriver().getLast_name() + " אישר את ההצטרפות שלך",
                                                    ApplicationContext, activity);
                                            notificationsSender.SendNotifications();

                                        }
                                    }

                                });


                            } else {
                                Toast.makeText(context, "Error: " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }

    public void reject_joining(Ride curr_ride, User curr_trempist) {
        myRef.child("rides").child(curr_ride.getId()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                } else {
                    // Adding the driver to the ride
//                    Ride ride = Objects.requireNonNull(task.getResult()).getValue(Ride.class);
                    curr_ride.remove_from_waitingList(curr_trempist);

                    myRef.child("rides").child(curr_ride.getId()).setValue(curr_ride).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                myRef.child("tokens").child(curr_trempist.getId()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                                        if (!task.isSuccessful()) {
                                            Log.e("firebase", "Error getting data", task.getException());
                                        } else {
                                            // Adding the driver to the ride
                                            String TOKEN = Objects.requireNonNull(task.getResult()).getValue(String.class);
                                            FcmNotificationsSender notificationsSender = new FcmNotificationsSender(
                                                    TOKEN,
                                                    "דחיית הצטרפות",
                                                    curr_ride.getDriver().getFirst_name() + " " + curr_ride.getDriver().getLast_name() + " דחה/תה את ההצטרפות שלך",
                                                    ApplicationContext, activity);
                                            notificationsSender.SendNotifications();

                                        }
                                    }

                                });


                            } else {
                                Toast.makeText(context, "Error: " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

    }
    public void updateRideOnDB_Cancel_request(Ride curr_ride) {
        String trempist_UID = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        myRef.child("users").child(trempist_UID).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                } else {
                    // Adding the driver to the ride
                    User Trempist = Objects.requireNonNull(task.getResult()).getValue(User.class);
                    curr_ride.remove_from_waitingList(Trempist);
                    myRef.child("rides").child(curr_ride.getId()).setValue(curr_ride).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(context, "הבקשה בוטלה", Toast.LENGTH_SHORT).show();
                                String driver_UID = curr_ride.getDriver().id;
                                myRef.child("tokens").child(driver_UID).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                                        if (!task.isSuccessful()) {
                                            Log.e("firebase", "Error getting data", task.getException());
                                        } else {

                                        }
                                    }

                                });


                            } else {
                                Toast.makeText(context, "Error: " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });


    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public void setApplicationContext(Context applicationContext) {
        this.ApplicationContext = ApplicationContext;
    }



}

