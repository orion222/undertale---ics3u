

Problems
Known bugs:
When you hold down two arrow keys and let go of the second key that you were holding down, 
the keyPressed method will stop running. Thus, there are situations where Chara gets stuck 
in the entrance/exit, and you have to let go or click another button to advance as usual.
She gets stuck as her position is not being updated until keyPressed is initiated.

In a map with an interactable that has not been interacted with, Chara and the sound
effects will freeze when clicking the interact button while moving.
