package jp.vmi.selenium.selenese.cmdproc;

import java.util.List;
import java.util.Map;

import org.apache.commons.io.output.StringBuilderWriter;
import org.json.JSONException;
import org.json.JSONObject;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.internal.seleniumemulation.CompoundMutator;

import com.thoughtworks.selenium.SeleniumException;

/**
 * Evaluator of script including "storedVars" variable.
 */
public class Eval {

    private final CompoundMutator mutator;
    private final Map<String, Object> varsMap;

    /**
     * Constructor.
     *
     * @param baseUrl base URL.
     * @param varsMap variable map.
     */
    public Eval(String baseUrl, Map<String, Object> varsMap) {
        this.mutator = new CompoundMutator(baseUrl);
        this.varsMap = varsMap;
    }

    /**
     * Evaluate script including "storedVars" variable.
     *
     * @param driver WebDriver instance.
     * @param script JavaScript code.
     * @return result of evaluating script.
     */
    public Object eval(WebDriver driver, String script) {
        boolean hasStoredVars = script.matches(".*\\bstoredVars\\b.*");
        StringBuilderWriter writer = new StringBuilderWriter();
        if (hasStoredVars) {
            writer.append("return (function(){var storedVars = ");
            try {
                new JSONObject(varsMap).write(writer);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            writer.append(";\n");
        }
        writer.append("return [(function(){");
        mutator.mutate(script, writer.getBuilder());
        writer.append("})()");
        if (hasStoredVars)
            writer.append(", storedVars");
        writer.append("];");
        if (hasStoredVars)
            writer.append("})();");
        Object result = ((JavascriptExecutor) driver).executeScript(writer.toString());
        if (!(result instanceof List))
            throw new SeleniumException(result.toString());
        List<?> list = (List<?>) result;
        switch (list.size()) {
        case 0:
            return null;
        case 1:
            return list.get(0);
        default: // case 2:
            @SuppressWarnings("unchecked")
            Map<String, Object> newVarsMap = (Map<String, Object>) list.get(1);
            varsMap.clear();
            varsMap.putAll(newVarsMap);
            return list.get(0);
        }
    }

}
