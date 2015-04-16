package com.helme.helpmebutton.ui;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import com.helme.helpmebutton.R;

import java.util.ArrayList;
import java.util.UUID;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, new PlaceholderFragment().newInstance(position + 1))
                .commit();
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_home);
                break;
            case 2:
                mTitle = getString(R.string.title_account);
                break;
            case 3:
                mTitle = getString(R.string.title_settings);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        private BluetoothAdapter mBluetoothAdapter = null;
        private boolean mScanning = false;
        private Handler mHandler = new Handler();
        private ListAdapter mLeDeviceListAdapter;
        private View rootView;

        private static final long SCAN_TIMEOUT = 30000;

        private class ViewHolder {
            public TextView text;
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            rootView = inflater.inflate(R.layout.fragment_main, container, false);

            final BluetoothManager bluetoothManager =
                    (BluetoothManager) getActivity().getSystemService(Context.BLUETOOTH_SERVICE);
            mBluetoothAdapter = bluetoothManager.getAdapter();

            Button scanButton = (Button) rootView.findViewById(R.id.scanButton);
            scanButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onScan(v);
                }
            });

            mLeDeviceListAdapter = new ListAdapter();

            ((ListView) rootView.findViewById(R.id.deviceList)).setAdapter(mLeDeviceListAdapter);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }

        public void onScan(View view) {
            // check bluetooth is available on on
            if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivity(enableBtIntent);
                return;
            }

            scanLeDevice(!mScanning);
        }

        private void setScanState(boolean value) {
            mScanning = value;

            Button scanButton = (Button) rootView.findViewById(R.id.scanButton);
            scanButton.setText(value ? "Stop" : "Scan");
        }

        private void scanLeDevice(final boolean enable) {
            if (enable) {
                // scan for SCAN_TIMEOUT
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        setScanState(false);
                        mBluetoothAdapter.stopLeScan(mLeScanCallback);
                    }
                }, SCAN_TIMEOUT);

                setScanState(true);
                mLeDeviceListAdapter.clear();
                mLeDeviceListAdapter.notifyDataSetChanged();
                // pass in the link loss service id uuid to filter out devices that dont support it
                UUID[] uuids = new UUID[1];
                uuids[0] = UUID.fromString("00001803-0000-1000-8000-00805f9b34fb");
//                uuids[0] = UUID.fromString("B8E06067-62AD-41BA-9231-206AE80AB550");

                mBluetoothAdapter.startLeScan(uuids, mLeScanCallback);
            } else {
                setScanState(false);
                mBluetoothAdapter.stopLeScan(mLeScanCallback);
            }
        }

        // Device scan callback.
        private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {

            @Override
            public void onLeScan(final BluetoothDevice device, int rssi,
                                 byte[] scanRecord) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        mLeDeviceListAdapter.addDevice(device);
                        mLeDeviceListAdapter.notifyDataSetChanged();
                    }
                });
            }
        };

        // adaptor
        private class ListAdapter extends BaseAdapter {
            private ArrayList<BluetoothDevice> mLeDevices;

            public ListAdapter() {
                super();
                mLeDevices = new ArrayList<BluetoothDevice>();

            }

            public void addDevice(BluetoothDevice device) {
                if (!mLeDevices.contains(device)) {
                    mLeDevices.add(device);
                }
            }

            public BluetoothDevice getDevice(int position) {
                return mLeDevices.get(position);
            }

            public void clear() {
                mLeDevices.clear();
            }

            @Override
            public int getCount() {
                return mLeDevices.size();
            }

            @Override
            public Object getItem(int i) {
                return mLeDevices.get(i);
            }

            @Override
            public long getItemId(int i) {
                return i;
            }

            @Override
            public View getView(int i, View view, ViewGroup viewGroup) {
                ViewHolder viewHolder;
                // General ListView optimization code.
                if (view == null) {
                    view = getActivity().getLayoutInflater().inflate(R.layout.list_row, null);
                    viewHolder = new ViewHolder();
                    viewHolder.text = (TextView) view.findViewById(R.id.textView);
                    view.setTag(viewHolder);
                } else {
                    viewHolder = (ViewHolder) view.getTag();
                }
                BluetoothDevice device = mLeDevices.get(i);
                final String deviceName = device.getName();
                if (deviceName != null && deviceName.length() > 0)
                    viewHolder.text.setText(deviceName);
                else
                    viewHolder.text.setText("unknown device");

                return view;
            }
        }
    }
}
