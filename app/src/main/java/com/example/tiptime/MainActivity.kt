package com.example.tiptime

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.StringRes
import androidx.annotation.VisibleForTesting
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tiptime.ui.theme.TipTimeTheme
import java.text.NumberFormat

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TipTimeTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background) {
                  TipTimeScreen()
                }
            }
        }
    }
}


@Composable
fun TipTimeScreen(){
    var tipInput by remember {
        mutableStateOf("")
    }
    val tipPercent = tipInput.toDoubleOrNull() ?: 0.0

    var amountInput by remember {
        mutableStateOf("")
    }
    val amount = amountInput.toDoubleOrNull() ?:0.0
    //The LocalFocusManager interface is used to control focus in Compose
    val focusManager= LocalFocusManager.current

    var roundUp by remember {
        mutableStateOf(false)
    }
    val tip = calculateTip(amount,tipPercent, roundUp )

    Column(
        modifier = Modifier.padding(32.dp),
        //This adds a fixed 8dp space between child elements.
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ){
        Text(
            text = stringResource(R.string.calculate_tip),
            fontSize = 24.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Spacer(Modifier.height(16.dp))
        EditNumberField(
            label = R.string.bill_amount,
            value = amountInput,
            onValueChange = {amountInput=it},
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    focusManager.moveFocus(FocusDirection.Down)
                }

            )
        )
        EditNumberField(
            label = R.string.tip_amount,
            value = tipInput,
            onValueChange = {tipInput=it},
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                }
            )
        )
        RoundTheTip(roundUp = roundUp, onRoundUpChanged = {roundUp=it} )
        Spacer(Modifier.height(24.dp))

        Text(
            text = stringResource(R.string.tip_amount, tip),
            modifier = Modifier.align(Alignment.CenterHorizontally),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
    }

}

@Composable
fun EditNumberField(
    @StringRes label:Int,
    value :String,
    onValueChange : (String) -> Unit,
    modifier : Modifier = Modifier,
    keyboardOptions: KeyboardOptions,
    keyboardActions: KeyboardActions
){

    TextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(stringResource(label)) },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions
    )


}

@VisibleForTesting
internal fun calculateTip(
    amount : Double,
    tipPercent: Double = 15.0,
    roundUp: Boolean
    ): String{
    var tip = tipPercent /100 * amount
    if(roundUp){
        //Rounds the given value x to an integer towards positive infinity
        kotlin.math.ceil(tip)
    }
    //This gives you a number formatter that you can use to format numbers as currency.
     return NumberFormat.getCurrencyInstance().format(tip)


}


@Composable
fun RoundTheTip(
    roundUp:Boolean,
    onRoundUpChanged:(Boolean)->Unit,
    ){
   Row(
       modifier = Modifier
           .fillMaxWidth()
           .size(48.dp),
       verticalAlignment = Alignment.CenterVertically
   ){
       Text(text= stringResource(R.string.round_up_tip))
       Switch(
           checked = roundUp,
           onCheckedChange = onRoundUpChanged,
           modifier = Modifier
               .fillMaxWidth()
               .wrapContentWidth(Alignment.End),
       colors=SwitchDefaults.colors(
           uncheckedThumbColor = Color.Magenta,
           checkedThumbColor = Color.Red
       )
           )

   }

}
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    TipTimeTheme {
       TipTimeScreen()
    }
}