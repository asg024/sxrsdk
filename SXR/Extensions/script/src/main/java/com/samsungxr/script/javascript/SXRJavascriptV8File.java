package com.samsungxr.script.javascript;

import java.util.Map;

import com.samsungxr.utility.Log;

import com.samsungxr.SXRContext;

import javax.script.Invocable;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptException;
import javax.script.Bindings;

import lu.flier.script.V8ScriptEngineFactory;


/**
 * Represents a Javascript file that will be compiled and run by the V8 engine
 *
 * Once a script text is set or loaded, you can invoke functions in the
 * script using nvokeFunction(String functionName, Object[] parameters)},
 * to handle events delivered to it.
 */

public class SXRJavascriptV8File {

    /**
     * Loads a Javascript file from a text string.
     *
     * @param gvrContext
     *     The SXR Context.
     * @param scriptText
     *     String containing a Javascript program.
     */
    private static final String TAG = SXRJavascriptV8File.class.getSimpleName();

    protected String mScriptText;
    protected SXRContext mGvrContext = null;
    static protected ScriptEngine mEngine = null;
    protected Bindings bindings = null;
    protected Invocable invocable = null;
    protected Bindings inputBindings = null;
    Map inputVars = null;


    public SXRJavascriptV8File(SXRContext gvrContext, String scriptText) {

        mScriptText = scriptText;
        mGvrContext = gvrContext;
    }

    public void setInputValues(Map inputValues) {
        inputVars = inputValues;
    }

    public boolean invokeFunction(String funcName, Object[] parameters, String paramString) {
        boolean runs = false;
        try {
            if ( mEngine == null ) {
                mEngine = new V8ScriptEngineFactory().getScriptEngine();
            }
            if ( inputVars != null ) {
                Bindings inputBindings = mEngine.createBindings();
                inputBindings.putAll(inputVars);
            }

            mEngine.eval( paramString );
            mEngine.eval( mScriptText );

            invocable = (Invocable) mEngine;
             invocable.invokeFunction(funcName, parameters);
            bindings = mEngine.getBindings( ScriptContext.ENGINE_SCOPE);
            runs = true;
        } catch (ScriptException e) {
            Log.d(TAG, "ScriptException: " + e);
            Log.d(TAG, "   function: '" + funcName + "'");
            Log.d(TAG, "   input Variables: '" + paramString + "'");
            Log.d(TAG, "   JavaScript:\n" + mScriptText);
        } catch (Exception e) {
            Log.d(TAG, "Exception: " + e);
            Log.d(TAG, "   function: '" + funcName + "'");
            Log.d(TAG, "   input Variables: '" + paramString + "'");
            Log.d(TAG, "   JavaScript:\n" + mScriptText);
        }
        return runs;
    }

    /**
     * Access to values modified during invoking of Script file
     * Enables X3D to get values script modifies..
     * @return
     */
    public Bindings getLocalBindings() {
        return bindings;
    }

    /**
     * Sets the script file.
     */
    public void setScriptText(String scriptText) {
        mScriptText = scriptText;
    }

    /**
     * Gets the script file.
     * @return The script string.
     */
    public String getScriptText() {
        return mScriptText;
    }

}
