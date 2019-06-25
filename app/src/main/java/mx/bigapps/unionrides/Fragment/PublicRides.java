package mx.bigapps.unionrides.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import mx.bigapps.unionrides.Activity.ExpandableHeightGridView;
import mx.bigapps.unionrides.Activity.Rides;
import mx.bigapps.unionrides.Adapter.rides_Adapter;
import mx.bigapps.unionrides.R;


public class PublicRides extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    rides_Adapter ridesphoto_adapter;
    ExpandableHeightGridView photolistView;
    ArrayList photolist;
    Button addRides;
    TextView txtrides;


    public PublicRides() {
        // Required empty public constructor
    }

    public static PublicRides newInstance(String param1, String param2) {
        PublicRides fragment = new PublicRides();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_public_rides, container, false);

        photolistView = (ExpandableHeightGridView) view.findViewById(R.id.photolist);
        addRides = (Button) view.findViewById(R.id.addRides);


        photolist = new ArrayList();
        photolist.add("");
        photolist.add("");
        photolist.add("");
        photolist.add("");
        photolist.add("");
        photolist.add("");
        photolist.add("");
        photolist.add("");
        photolist.add("");
        photolist.add("");
        ridesphoto_adapter = new rides_Adapter(getActivity(), photolist);
        photolistView.setAdapter(ridesphoto_adapter);
        photolistView.smoothScrollToPosition(0);

        addRides.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Rides.class);
                startActivity(intent);

               /* final Dialog dialog = new Dialog(RidesFragmnts.this, R.style.DialogSlideAnim);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.addridedialog_custom);

                dialog.getWindow().setBackgroundDrawable(
                        new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dialog.show();
                RelativeLayout takepicture = (RelativeLayout) dialog.findViewById(R.id.gallery_rl);
                RelativeLayout uploadfile = (RelativeLayout) dialog.findViewById(R.id.camera_rl);
                RelativeLayout cancel_rl = (RelativeLayout) dialog.findViewById(R.id.cancel_rl);
                cancel_rl.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();

                    }
                });
                takepicture.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        ContentValues values = new ContentValues();
                        values.put(MediaStore.Images.Media.TITLE, "New Picture");
                        values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");

                        //  imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                        *//*Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                        startActivityForResult(intent, 1);*//*
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent, 7);
                        dialog.dismiss();

                    }
                });*/


            }
        });


        return view;

    }


}
