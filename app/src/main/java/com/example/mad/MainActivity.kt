package com.example.mad

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mad.ui.theme.MADTheme
import java.math.RoundingMode
import java.text.DecimalFormat

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        basicScreenLayout()
    }

    private fun basicScreenLayout() {
        setContent {
            MADTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    RollDiceScreen()
                }
            }
        }
    }
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
private fun RollDiceScreen() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.app_name)) }
            )
        },
        content = {  ScreenContent() }
    )
}

//unused modifier for now
@Composable
private fun ScreenContent(
) {
    var currentDiceValue: Int by remember { mutableStateOf(getRandomNumber()) } // Initial dice roll.
    var nrRolls: Int by remember { mutableStateOf(1) } // Value 1 after the initial dice roll.
    var totalOfRolledDiceValues: Int by remember { mutableStateOf(currentDiceValue) }
    val averageDiceValueRounded = roundOffDecimal(totalOfRolledDiceValues / nrRolls.toDouble())

    Column(
        Modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Instruction()
        ImageToDisplay( diceValue = currentDiceValue )
        AverageDiceValue(
            averageDiceValue = averageDiceValueRounded
            , nrRolls = nrRolls
        )
        NextRoll(
            updateDiceValuesAndNrRolls = { newDiceValue: Int ->
                currentDiceValue = newDiceValue
                nrRolls++
                totalOfRolledDiceValues += newDiceValue
            }
        )
    }
}

@Composable
private fun Instruction() {
    Text (text = "Please click the button to roll the dice. The average value of the dice will be displayed below the dice.")
}

@Composable
private fun ImageToDisplay( diceValue: Int ) {
    Image(
        /* TODO Image ID to be provided later. */
        painter = painterResource(id = diceValueToImageId(diceValue)),
        contentDescription = "dice",
        modifier = Modifier
            .padding(all = 64.dp)
            .width(250.dp)
            .height(250.dp)
    )



}

@Composable
private fun AverageDiceValue( averageDiceValue: Double , nrRolls: Int ) {
    Text(
        text = stringResource(id = R.string.average, averageDiceValue, nrRolls),
        style = MaterialTheme.typography.h6,
    )
}

@Composable
private fun NextRoll( updateDiceValuesAndNrRolls: (Int) -> Unit) {
    Button(onClick = {
        updateDiceValuesAndNrRolls(getRandomNumber())
    }) {
        Text(text = "Roll the dice")
    }

}

private fun getRandomNumber(): Int {
    return (1..6).random()
}

private fun roundOffDecimal(number: Double): Double {
    val df = DecimalFormat("#.#")
    df.roundingMode = RoundingMode.CEILING
    return df.format(number).toDouble()
}

// Dice value is a number between 1 and 6, including. So return the appropriate array element.
private fun diceValueToImageId(diceValue: Int): Int {
    val diceValues = arrayOf(
        R.drawable.dice1, R.drawable.dice2, R.drawable.dice3,
        R.drawable.dice4, R.drawable.dice5, R.drawable.dice6
    )
    return diceValues[diceValue - 1]
}

@Preview(showBackground = true)
@Composable
private fun DefaultPreview() {
    MADTheme {
        RollDiceScreen()
    }
}