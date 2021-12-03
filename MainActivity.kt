package com.example.grammarmemorygame

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.grammarmemorygame.databinding.ActivityMainBinding
import android.util.Log
import android.widget.ImageButton
import android.widget.Toast


// This has all the logic associated with the game
private const val TAG = "MainActivity"
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var buttons: List<ImageButton> //declare this outside on Create to use elsewhere
    private lateinit var cards: List<MemoryCard>
    private var indexOfSingleSelectedCard: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view:ConstraintLayout = binding.root
        setContentView(view)

        val images = mutableListOf(
            R.drawable.noun,
            R.drawable.noun2,
            R.drawable.verb,
            R.drawable.verb2,
            R.drawable.adverb,
            R.drawable.adverb2,
            R.drawable.adjective,
            R.drawable.adjective2,
            R.drawable.pronoun,
            R.drawable.pronoun2,
            R.drawable.prep,
            R.drawable.prep2)

        //Randomize the images
        images.shuffle()

        buttons = listOf(
            binding.imageButton1,
            binding.imageButton2,
            binding.imageButton3,
            binding.imageButton4,
            binding.imageButton5,
            binding.imageButton6,
            binding.imageButton7,
            binding.imageButton8,
            binding.imageButton9,
            binding.imageButton10,
            binding.imageButton11,
            binding.imageButton12
        )
        //This gives us an indices to map
        cards = buttons.indices.map { index ->
            MemoryCard(images[index])

        }
        //This directs the state of the button making it face up or face down
        buttons.forEachIndexed { index, button ->
            button.setOnClickListener {
                Log.i(TAG, "clicked")
                //update models
                updateModels(index)
                //Change the UI Views
                updateViews()

            }
        }
    }

    private fun updateViews() {
        cards.forEachIndexed { index, card ->
            val button = buttons[index]
            button.setImageResource(if (card.isFaceUp) card.identifier else R.drawable.ic_baseline_device_unknown_24)
        }
    }
    private fun updateModels(position:Int) {
        val card = cards[position]
        //Error checks:
        if (card.isFaceUp) {
            Toast.makeText(this,"Invalid move", Toast.LENGTH_SHORT).show()
            return
        }
        if (indexOfSingleSelectedCard == null) {
            //possibility one: 0  or 2 cards flipped over
            restoreCards()
            indexOfSingleSelectedCard = position
        } else {
            //possibility two: 1 card flipped over
            checkForMatch(indexOfSingleSelectedCard!!, position)
            indexOfSingleSelectedCard = null
        }

        card.isFaceUp = !card.isFaceUp
    }

    private fun restoreCards() {
        for (card in cards) {
            if (!card.isMatched) {
                card.isFaceUp = false
            }
        }
    }
    private fun checkForMatch(position1: Int, position2: Int){
        if ((cards[position1].identifier == R.drawable.noun && cards[position2].identifier == R.drawable.noun2) ||
            (cards[position1].identifier == R.drawable.verb && cards[position2].identifier == R.drawable.verb2) ||
            (cards[position1].identifier == R.drawable.adverb && cards[position2].identifier == R.drawable.adverb2) ||
            (cards[position1].identifier == R.drawable.adjective && cards[position2].identifier == R.drawable.adjective2) ||
            (cards[position1].identifier == R.drawable.pronoun && cards[position2].identifier == R.drawable.pronoun2) ||
            (cards[position1].identifier == R.drawable.prep && cards[position2].identifier == R.drawable.prep2) ||
            (cards[position1].identifier == R.drawable.noun2 && cards[position2].identifier == R.drawable.noun) ||
            (cards[position1].identifier == R.drawable.verb2 && cards[position2].identifier == R.drawable.verb) ||
            (cards[position1].identifier == R.drawable.adverb2 && cards[position2].identifier == R.drawable.adverb) ||
            (cards[position1].identifier == R.drawable.adjective2 && cards[position2].identifier == R.drawable.adjective) ||
            (cards[position1].identifier == R.drawable.pronoun2 && cards[position2].identifier == R.drawable.pronoun) ||
            (cards[position1].identifier == R.drawable.prep2 && cards[position2].identifier == R.drawable.prep)) {
            Toast.makeText(this, "Correct!", Toast.LENGTH_SHORT).show()
            cards[position1].isMatched = true
            cards[position2].isMatched = true
        }
    }
}