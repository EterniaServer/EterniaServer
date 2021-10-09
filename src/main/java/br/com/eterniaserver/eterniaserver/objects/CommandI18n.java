package br.com.eterniaserver.eterniaserver.objects;

public class CommandI18n {

    private final String key;
    private String name;
    private String syntax;
    private String description;
    private String perm;

    public CommandI18n(final String key,
                       final String name,
                       final String syntax,
                       final String description,
                       final String perm) {
        this.key = key;
        this.name = name;
        this.syntax = syntax;
        this.description = description;
        this.perm = perm;
    }

    private String validate(final String string) {
        return string != null ? string : "";
    }

    public String key() {
        return validate(key);
    }

    public String name() {
        return validate(name);
    }

    public void name(String string) {
        name = string;
    }

    public String syntax() {
        return validate(syntax);
    }

    public void syntax(String string) {
        syntax = string;
    }

    public String description() {
        return validate(description);
    }

    public void description(String string) {
        description = string;
    }

    public String perm() {
        return validate(perm);
    }

    public void perm(String string) {
        perm = string;
    }

}
