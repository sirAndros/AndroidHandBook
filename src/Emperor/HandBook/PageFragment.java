package Emperor.HandBook;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Emperor on 17.04.14.
 */
public class PageFragment extends Fragment {
    static final String ARGUMENT_PAGE_ITEM_TYPE  = "arg_page_number";

    View notes;
    View tags;

    ItemTypes itemType;

    static PageFragment newInstance(int page) {
        PageFragment pageFragment = new PageFragment();
        Bundle arguments = new Bundle();
        arguments.putInt(ARGUMENT_PAGE_ITEM_TYPE, page);
        pageFragment.setArguments(arguments);
        return pageFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        itemType = ItemTypes.values()[getArguments().getInt(ARGUMENT_PAGE_ITEM_TYPE)];
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        switch (itemType) {
            case Note:
                if (notes == null)
                    notes = inflater.inflate(R.layout.notes, null);
                return notes;
            case Tag:
                if (tags == null)
                    tags = inflater.inflate(R.layout.tags, null);
                return tags;
        }
        return null;
    }


    public View getNotesView() {
        return notes;
    }

    public View getTagsView() {
        return tags;
    }
}