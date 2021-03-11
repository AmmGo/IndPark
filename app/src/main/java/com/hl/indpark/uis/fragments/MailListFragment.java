package com.hl.indpark.uis.fragments;


import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.hl.indpark.R;
import com.hl.indpark.entities.Response;
import com.hl.indpark.entities.events.PhoneEvent;
import com.hl.indpark.nets.ApiObserver;
import com.hl.indpark.nets.repositories.ArticlesRepo;
import com.hl.indpark.utils.CharacterParser;
import com.hl.indpark.utils.ContactsSortAdapter;
import com.hl.indpark.utils.PinyinComparator;
import com.hl.indpark.utils.SideBar;
import com.hl.indpark.utils.SortModel;
import com.hl.indpark.utils.SortToken;

import net.arvin.baselib.base.BaseFragment;
import net.arvin.baselib.utils.ToastUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * Created by yjl on 2021/3/8 11:05
 * Function：
 * Desc：通讯录
 */
public class MailListFragment extends BaseFragment {


    ListView mListView;
    EditText etSearch;
    ImageView ivClearText;

    private SideBar sideBar;
    private TextView dialog;

    private List<SortModel> mAllContactsList;
    private ContactsSortAdapter adapter;
    /**
     * 汉字转换成拼音的类
     */
    private CharacterParser characterParser;

    /**
     * 根据拼音来排列ListView里面的数据类
     */
    private PinyinComparator pinyinComparator;

    @Override
    protected int getContentView() {
        return R.layout.fragment_maillist;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        initView();
        initListener();
        getData();
    }

    public void getData() {
        ArticlesRepo.getPhoneEvent().observe(this, new ApiObserver<List<PhoneEvent>>() {
            @Override
            public void onSuccess(Response<List<PhoneEvent>> response) {
                List<PhoneEvent> data = response.getData();
                if (data != null && data.size() > 0) {
                    loadContacts(data);
                }
                Log.e("人员列表", "onSuccess: ");
            }

            @Override
            public void onFailure(int code, String msg) {
                super.onFailure(code, msg);
            }

            @Override
            public void onError(Throwable throwable) {
                super.onError(throwable);

            }
        });
    }

    private void initListener() {

        /**清除输入字符**/
        ivClearText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                etSearch.setText("");
            }
        });
        etSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

            }

            @Override
            public void afterTextChanged(Editable e) {

                String content = etSearch.getText().toString();
                if ("".equals(content)) {
                    ivClearText.setVisibility(View.INVISIBLE);
                } else {
                    ivClearText.setVisibility(View.VISIBLE);
                }
                if (content.length() > 0) {
                    ArrayList<SortModel> fileterList = (ArrayList<SortModel>) search(content);
                    adapter.updateListView(fileterList);
                    //mAdapter.updateData(mContacts);
                } else {
                    adapter.updateListView(mAllContactsList);
                }
                mListView.setSelection(0);

            }

        });

        //设置右侧[A-Z]快速导航栏触摸监听
        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                //该字母首次出现的位置
                int position = adapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    mListView.setSelection(position);
                }
            }
        });
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long arg3) {
                ContactsSortAdapter.ViewHolder viewHolder = (ContactsSortAdapter.ViewHolder) view.getTag();
              SortModel sortModel =   adapter.toggleChecked(position);
                ToastUtil.showToast(getActivity(), "点击了"+sortModel.name);
            }
        });

    }

    private void initView() {
        sideBar = (SideBar) root.findViewById(R.id.sidrbar);
        dialog = (TextView) root.findViewById(R.id.dialog);
        sideBar.setTextView(dialog);
        ivClearText = (ImageView) root.findViewById(R.id.ivClearText);
        etSearch = (EditText) root.findViewById(R.id.et_search);
        mListView = (ListView) root.findViewById(R.id.lv_contacts);

        /** 给ListView设置adapter **/
        characterParser = CharacterParser.getInstance();
        mAllContactsList = new ArrayList<SortModel>();
        pinyinComparator = new PinyinComparator();
        Collections.sort(mAllContactsList, pinyinComparator);// 根据a-z进行排序源数据
        adapter = new ContactsSortAdapter(getActivity(), mAllContactsList);
        mListView.setAdapter(adapter);
    }

    private void loadContacts(List<PhoneEvent> data) {
        mAllContactsList = new ArrayList<SortModel>();
        for (int i = 0; i < data.size(); i++) {
            //优先使用系统sortkey取,取不到再使用工具取
            String sortLetters = getSortLetterBySortKey(data.get(i).name);
            if (sortLetters == null) {
                sortLetters = getSortLetter(data.get(i).name);
            }
            SortModel sortModel = new SortModel(data.get(i).name, data.get(i).phone, data.get(i).name, data.get(i).sex);
            sortModel.sortLetters = sortLetters;

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
                sortModel.sortToken = parseSortKey(data.get(i).name);
            else
                sortModel.sortToken = parseSortKeyLollipop(data.get(i).name);
            mAllContactsList.add(sortModel);
        }
        Collections.sort(mAllContactsList, pinyinComparator);
        adapter.updateListView(mAllContactsList);
    }

    /**
     * 名字转拼音,取首字母
     *
     * @param name
     * @return
     */
    private String getSortLetter(String name) {
        String letter = "#";
        if (name == null) {
            return letter;
        }
        //汉字转换成拼音
        String pinyin = characterParser.getSelling(name);
        String sortString = pinyin.substring(0, 1).toUpperCase(Locale.CHINESE);

        // 正则表达式，判断首字母是否是英文字母
        if (sortString.matches("[A-Z]")) {
            letter = sortString.toUpperCase(Locale.CHINESE);
        }
        return letter;
    }

    /**
     * 取sort_key的首字母
     *
     * @param sortKey
     * @return
     */
    private String getSortLetterBySortKey(String sortKey) {
        if (sortKey == null || "".equals(sortKey.trim())) {
            return null;
        }
        String letter = "#";
        //汉字转换成拼音
        String sortString = sortKey.trim().substring(0, 1).toUpperCase(Locale.CHINESE);
        // 正则表达式，判断首字母是否是英文字母
        if (sortString.matches("[A-Z]")) {
            letter = sortString.toUpperCase(Locale.CHINESE);
        } else {
            letter = getSortLetter(sortString.toUpperCase(Locale.CHINESE));
        }
        return letter;
    }
//    else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {// 5.0以上需要判断汉字
//        if (isChinese(sortString)){
//            letter = getSortLetter(sortString.toUpperCase(Locale.CHINESE));
//        } else{
//            letter = getSortLetter(sortString.toUpperCase(Locale.CHINESE));
//        }
//    }

    /**
     * 判断是否为汉字
     *
     * @param string
     * @return
     */

    public static boolean isChinese(String string) {
        int n = 0;
        for (int i = 0; i < string.length(); i++) {
            n = (int) string.charAt(i);
            if (!(19968 <= n && n < 40869)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 模糊查询
     *
     * @param str
     * @return
     */
    private List<SortModel> search(String str) {
        List<SortModel> filterList = new ArrayList<SortModel>();// 过滤后的list
        //if (str.matches("^([0-9]|[/+])*$")) {// 正则表达式 匹配号码
        if (str.matches("^([0-9]|[/+]).*")) {// 正则表达式 匹配以数字或者加号开头的字符串(包括了带空格及-分割的号码)
            String simpleStr = str.replaceAll("\\-|\\s", "");
            for (SortModel contact : mAllContactsList) {
                if (contact.number != null && contact.name != null) {
                    if (contact.simpleNumber.contains(simpleStr) || contact.name.contains(str)) {
                        if (!filterList.contains(contact)) {
                            filterList.add(contact);
                        }
                    }
                }
            }
        } else {
            for (SortModel contact : mAllContactsList) {
                if (contact.number != null && contact.name != null) {
                    //姓名全匹配,姓名首字母简拼匹配,姓名全字母匹配
                    boolean isNameContains = contact.name.toLowerCase(Locale.CHINESE)
                            .contains(str.toLowerCase(Locale.CHINESE));

                    boolean isSortKeyContains = contact.sortKey.toLowerCase(Locale.CHINESE).replace(" ", "")
                            .contains(str.toLowerCase(Locale.CHINESE));

                    boolean isSimpleSpellContains = contact.sortToken.simpleSpell.toLowerCase(Locale.CHINESE)
                            .contains(str.toLowerCase(Locale.CHINESE));

                    boolean isWholeSpellContains = contact.sortToken.wholeSpell.toLowerCase(Locale.CHINESE)
                            .contains(str.toLowerCase(Locale.CHINESE));

                    if (isNameContains || isSortKeyContains || isSimpleSpellContains || isWholeSpellContains) {
                        if (!filterList.contains(contact)) {
                            filterList.add(contact);
                        }
                    }
                }
            }
        }
        return filterList;
    }

    /**
     * 中文字符串匹配
     */
    String chReg = "[\\u4E00-\\u9FA5]+";

    //String chReg="[^\\u4E00-\\u9FA5]";//除中文外的字符匹配

    /**
     * 解析sort_key,封装简拼,全拼
     *
     * @param sortKey
     * @return
     */
    public SortToken parseSortKey(String sortKey) {
        SortToken token = new SortToken();
        if (sortKey != null && sortKey.length() > 0) {
            //其中包含的中文字符
            String[] enStrs = sortKey.replace(" ", "").split(chReg);
            for (int i = 0, length = enStrs.length; i < length; i++) {
                if (enStrs[i].length() > 0) {
                    //拼接简拼
                    token.simpleSpell += enStrs[i].charAt(0);
                    token.wholeSpell += enStrs[i];
                }
            }
        }
        return token;
    }

    /**
     * 解析sort_key,封装简拼,全拼。
     * Android 5.0 以上使用
     *
     * @param sortKey
     * @return
     */
    public SortToken parseSortKeyLollipop(String sortKey) {
        SortToken token = new SortToken();
        if (sortKey != null && sortKey.length() > 0) {
            boolean isChinese = sortKey.matches(chReg);
            // 分割条件：中文不分割，英文以大写和空格分割
            String regularExpression = isChinese ? "" : "(?=[A-Z])|\\s";

            String[] enStrs = sortKey.split(regularExpression);

            for (int i = 0, length = enStrs.length; i < length; i++)
                if (enStrs[i].length() > 0) {
                    //拼接简拼
                    token.simpleSpell += getSortLetter(String.valueOf(enStrs[i].charAt(0)));
                    token.wholeSpell += characterParser.getSelling(enStrs[i]);
                }
        }
        return token;
    }
}
