package org.pdapps.texte.model.toolbar;

import android.view.View;
import android.widget.ToggleButton;

import org.pdapps.texte.R;
import org.pdapps.texte.binding.toolbar.ToolbarViewType;
import org.pdapps.texte.model.action.Action;
import org.pdapps.texte.model.Model;

import static org.pdapps.texte.binding.toolbar.ToolbarViewType.PLANE;
import static org.pdapps.texte.binding.toolbar.ToolbarViewType.TOGGLE;

public enum ToolbarItem implements ToolbarString<ToolbarItem> {
    COPY(R.drawable.ic_content_copy_24dp, "Copy", PLANE, (model, toolbarString, view) -> {
        model.copyText();
    }),
    PASTE(R.drawable.ic_content_paste_24dp, "Paste", PLANE, ((model, toolbarString, view) -> {
        model.paste();
    })),
    CUT(R.drawable.ic_content_cut_24dp, "Cut", PLANE, ((model, toolbarString, view) -> {
        model.cut();
    })),
    SEARCH(R.drawable.ic_search_24dp, "Search", TOGGLE, ((model, toolbarString, view) -> {
        model.changeSearchBarVisibility(((ToggleButton)view).isChecked());
        model.saveAction(new Action("search"));
    })),
    GO_TO_LINE(R.drawable.ic_go_to_line_black_24dp, "Go to line", PLANE,
        (model, toolbarString, view) -> {
            model.goToLineDialog();
            model.saveAction(new Action("go_to_line"));
        }),
    TEXT_WRAP(R.drawable.ic_wrap_text_black_24dp, "Text Wrap", TOGGLE,
        (model, toolbarString, view) -> {
            model.setTextWrap(((ToggleButton) view).isChecked());
            model.saveAction(new Action("Text Wrap"));
        }),
    UN_INDENT(R.drawable.ic_format_unindent_black_24dp, "Un indent", PLANE,
        (model, toolbarString, view) -> {
            model.unIndent();
            model.saveAction(new Action("unindent"));
        }),
    INDENT(R.drawable.ic_format_indent_increase_black_24dp, "Indent", PLANE,
        (model, toolbarString, view) -> {
            model.indent();
            model.saveAction(new Action("indent"));
        });

    private final int iconId;
    private final ToolbarAction action;
    private final String contentDescription;
    private ToolbarViewType viewType;

    ToolbarItem(int iconId, String contentDescription, ToolbarViewType viewType,
        ToolbarAction toolbarAction) {
        this.iconId = iconId;
        this.action = toolbarAction;
        this.contentDescription = contentDescription;
        this.viewType = viewType;
    }

    @Override public void execute(Model model, View view) {
        action.execute(model, this, view);
    }

    @Override public int getIconId() {
        return iconId;
    }

    @Override public String getContentDescription() {
        return contentDescription;
    }

    public ToolbarViewType getViewType() {
        return viewType;
    }
}
