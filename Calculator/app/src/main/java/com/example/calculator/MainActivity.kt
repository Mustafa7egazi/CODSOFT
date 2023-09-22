package com.example.calculator


import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.View.OnTouchListener
import androidx.appcompat.app.AppCompatActivity
import com.example.calculator.databinding.ActivityMainBinding
import javax.script.ScriptEngineManager
import javax.script.ScriptException

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var shownTextOnScreen: String
    private var checkOperator = 0
    private var dotClicked = false
    private var plusClicked = false
    private var minusClicked = false
    private var multiplyClicked = false
    private var divideClicked = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val otl = OnTouchListener { _, _ ->  true }
        binding.userNumberInput.apply {
            movementMethod = ScrollingMovementMethod()
            setOnTouchListener(otl)
        }

        handleButtonsClick()

    }

    private fun handleButtonsClick() {
        val input = binding.userNumberInput


        binding.btn0.setOnClickListener {
            shownTextOnScreen = input.text.toString() + "0"
            input.setText(shownTextOnScreen)
            showResult(shownTextOnScreen)

            resetOperators()

        }

        binding.btn1.setOnClickListener {
            shownTextOnScreen = input.text.toString() + "1"
            input.setText(shownTextOnScreen)
            showResult(shownTextOnScreen)

            resetOperators()
        }

        binding.btn2.setOnClickListener {
            shownTextOnScreen = input.text.toString() + "2"
            input.setText(shownTextOnScreen)
            showResult(shownTextOnScreen)

            resetOperators()
        }

        binding.btn3.setOnClickListener {
            shownTextOnScreen = input.text.toString() + "3"
            input.setText(shownTextOnScreen)
            showResult(shownTextOnScreen)

            resetOperators()
        }

        binding.btn4.setOnClickListener {
            shownTextOnScreen = input.text.toString() + "4"
            input.setText(shownTextOnScreen)
            showResult(shownTextOnScreen)

            resetOperators()
        }
        binding.btn5.setOnClickListener {
            shownTextOnScreen = input.text.toString() + "5"
            input.setText(shownTextOnScreen)
            showResult(shownTextOnScreen)

            resetOperators()
        }
        binding.btn6.setOnClickListener {
            shownTextOnScreen = input.text.toString() + "6"
            input.setText(shownTextOnScreen)
            showResult(shownTextOnScreen)

            resetOperators()
        }
        binding.btn7.setOnClickListener {
            shownTextOnScreen = input.text.toString() + "7"
            input.setText(shownTextOnScreen)
            showResult(shownTextOnScreen)

            resetOperators()
        }
        binding.btn8.setOnClickListener {
            shownTextOnScreen = input.text.toString() + "8"
            input.setText(shownTextOnScreen)
            showResult(shownTextOnScreen)

            resetOperators()
        }
        binding.btn9.setOnClickListener {
            shownTextOnScreen = input.text.toString() + "9"
            input.setText(shownTextOnScreen)
            showResult(shownTextOnScreen)

            resetOperators()
        }
        binding.btnDot.setOnClickListener {
            if (!dotClicked) {
                shownTextOnScreen = input.text.toString() + "."
                if (shownTextOnScreen[0] == '.'){
                    shownTextOnScreen= "0." + shownTextOnScreen.substring(1)
                }else{
                    val indexOfLastDot = shownTextOnScreen.indexOfLast { it=='.' }
                    val isDigitBeforeDot = shownTextOnScreen[(indexOfLastDot-1)].isDigit()
                    if (!isDigitBeforeDot){
                        shownTextOnScreen =
                            shownTextOnScreen.substring(0, indexOfLastDot) + "0." + shownTextOnScreen.substring(indexOfLastDot + 1)
                    }
                }

                input.setText(shownTextOnScreen)
                showResult(shownTextOnScreen)
                dotClicked = true
            }
        }

        binding.plusBtn.setOnClickListener {
           if (!plusClicked){
               shownTextOnScreen = input.text.toString() + "+"
               input.setText(shownTextOnScreen)
               checkOperator++
               plusClicked = true
               dotClicked = false
           }
        }

        binding.minusBtn.setOnClickListener {
            if (!minusClicked){
                shownTextOnScreen = input.text.toString() + "-"
                input.setText(shownTextOnScreen)
                checkOperator++
                minusClicked = true
                dotClicked = false
            }
        }

        binding.multiplyBtn.setOnClickListener {
           if (!multiplyClicked){
               shownTextOnScreen = if (divideClicked || shownTextOnScreen.last() == '/'){
                   divideClicked = false
                   input.text.toString().replace( input.text.toString().last().toString() , "*")
               }else{
                   input.text.toString() + "*"
               }
               input.setText(shownTextOnScreen)
               checkOperator++
               multiplyClicked = true
               dotClicked = false
           }
        }

        binding.divideBtn.setOnClickListener {
           if (!divideClicked){
               shownTextOnScreen = if (multiplyClicked || shownTextOnScreen.last() == '*'){
                   multiplyClicked = false
                   input.text.toString().replace( input.text.toString().last().toString() , "/")
               }else{
                   input.text.toString() + "/"
               }
               input.setText(shownTextOnScreen)
               checkOperator++
               divideClicked = true
               dotClicked = false
           }
        }

        binding.btnPercent.setOnClickListener {
            shownTextOnScreen = input.text.toString() + "%"

            var newSubString = ""

                for (i in (shownTextOnScreen.lastIndex-1) downTo 0){
                    if (shownTextOnScreen[i] == '+' ||shownTextOnScreen[i] == '-' || shownTextOnScreen[i] == '*' ||shownTextOnScreen[i] == '/'){
                        break
                    }else{
                        newSubString+= shownTextOnScreen[i]
                    }
                }


            val numberToCalc = newSubString.reversed().toFloat()
            val engine = ScriptEngineManager().getEngineByName("rhino")
            val result = engine.eval("${numberToCalc}/100")


            val afterPercentResult = shownTextOnScreen.replace("${newSubString.reversed()}%",result.toString())
            //Toast.makeText(this,afterPercentResult,Toast.LENGTH_SHORT).show()
            input.setText(afterPercentResult)
            showResult(afterPercentResult)

            dotClicked = false

        }

        binding.btnEqual.setOnClickListener {
            shownTextOnScreen = binding.result.text.toString()
            input.setText(shownTextOnScreen)
            dotClicked = true
            binding.result.text = null
        }

        binding.clearBtn.setOnClickListener {
            input.text = null
            binding.result.text = null
        }

        binding.backspaceBtn.setOnClickListener {
            val expressionAfterBackspace: String?
            if (input.text.isNotEmpty()) {
                val sb = StringBuilder(input.text)
                val foundText = input.text.toString()
                val lastChar = foundText.last()
                if (lastChar == '+' || lastChar == '-' || lastChar == '*' || lastChar == '/' ) {
                    checkOperator--
                }
                sb.deleteCharAt(input.text.length - 1)
                expressionAfterBackspace = sb.toString()
                input.setText(expressionAfterBackspace)
                shownTextOnScreen = expressionAfterBackspace
                if (expressionAfterBackspace.length>1){
                    showResult(expressionAfterBackspace)
                }else{
                    binding.result.text.clear()
                    resetOperators()
                }

            }
        }
    }

    private fun resetOperators(){
        divideClicked = false
        plusClicked = false
        minusClicked = false
        multiplyClicked = false
    }

    private fun showResult(shownTextOnScreen: String) {
        val engine = ScriptEngineManager().getEngineByName("rhino")
        try {

            val result = engine.eval(shownTextOnScreen)

            if (checkOperator == 0) {
                binding.result.text = null
            } else {
                binding.result.setText(result.toString())
            }
        } catch (e: ScriptException) {
            Log.d("7egzz", "showResult: ${e.message}")
        }
    }
}