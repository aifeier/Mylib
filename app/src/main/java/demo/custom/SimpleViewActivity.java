package demo.custom;

import android.os.Bundle;
import android.view.View;

import com.cwf.app.cwf.R;
import com.cwf.libs.circularprogressbuttonlibrary.CircularProgressButton;

import lib.BaseActivity;
import lib.utils.AppUtils;
import lib.utils.entity.ContactsInfo;

/**
 * Created by n-240 on 2016/5/9.
 *
 * @author cwf
 * @email 237142681@qq.com
 */
public class SimpleViewActivity extends BaseActivity {
    private CircularProgressButton circularProgressButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_simple_view);
//        AppUtils.getLocalContactsInfos(this);
        ContactsInfo contactsInfo = new ContactsInfo();
        contactsInfo.setName("爱妃");
        contactsInfo.setPhone("15888888888");
        contactsInfo.setSortKey("爱妃");
        AppUtils.addContact(this, contactsInfo);
        circularProgressButton = (CircularProgressButton) findViewById(R.id.circular_progress_button);
        circularProgressButton.setIndeterminateProgressMode(true);
        circularProgressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                circularProgressButton.setProgress((int) (Math.random() * 101) - 1);
            }
        });
    }
}
