import java.io.PrintStream;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;
public class MyAI
implements AI
{
    public String getName()
    {
        return "MinMaxTree";
    }

    //Return the best move myPiece can make using a MinMaxTree
    public Point getNextMove(TicTacToe game, TicTacToePiece myPiece)
    {
        //if game is over, reutrn null
        try{
        Thread.sleep(1000);
        }catch(Exception e){
        }
        if(game.isGameOver())
        {
            return null;
        }
        //Generate a MinMax tree using the TicTacToe game parameter
        TNode<MinMaxNode> gameTree = generateMinMaxTree(game, null);
        //Score the MinMax tree
        scoreMinMaxTree(gameTree, myPiece);
        
        //if the root node has no children, return null
        if(gameTree.getChildren() == null)
            return null;
        
        //Create a list of MinMaxNodes from the children of the root of the MinMax tree you just generated
        List<TNode<MinMaxNode>> nodeList = new ArrayList<TNode<MinMaxNode>>();
        for(TNode<MinMaxNode> child: gameTree.getChildren()){
            nodeList.add(child);           
        }
        //Determine what the best (highest) score is among all of the MinMax nodes in the list your just created
        int highest = Integer.MIN_VALUE;
        for(TNode<MinMaxNode> node: nodeList)
        {
            if(node.getData().getScore() > highest)
                highest = node.getData().getScore();
        }
        //Create a list to hold all of the moves (Point objects) that are rated with the best score
        //loop through all of the children of the root of hte MinMax tree and add the Point from the ones with the "best score" to the list of Points you just created
        List<Point> bestList = new ArrayList<Point>();
        for(TNode<MinMaxNode> node: nodeList)
        {
            if(node.getData().getScore() == highest)
                bestList.add(node.getData().getMove());
        }
        //return a random point from the list of Points
        int r = (int)(Math.random() * bestList.size());
        return bestList.get(r);
    }

    //Generate a MinMaxTree consisting of a root node containing game, and children nodes 
    //containing all possible moves the current player can make
    private TNode<MinMaxNode> generateMinMaxTree(TicTacToe game, Point move)
    {
        //make a copy of game (so you can modify the copy without changing game)
        TicTacToe copy = game.copy();
        //if move is not null
        if(move != null)
        {
            //apply move to the copy (addPiece and nextPlayer)
            copy.addPiece(move.getRow(), move.getCol());
            copy.nextPlayer();
        }
            //Make a MinMaxNode with the copy and move
            //Make a TNode with the MinMaxNode you just created
        TNode<MinMaxNode> copyTNode = new TNode<MinMaxNode>(new MinMaxNode(copy, move));
            //recursively call generateMinMaxTree for each legal move that the new current player can make on copy (every empty space is a legal move)
            //add the TNode returned by the recursive generateMinMaxTree calls as a child to the TNode you created above
        for(Point point: copy.getEmptySpaces()){
            TNode<MinMaxNode> n = generateMinMaxTree(copy, point);
            copyTNode.addChild(n);
                }
            
        //return the TNode you created above
        return copyTNode;
        
        
    }

    //Generate a score for every node in the MinMaxTree (from the point of view of player)
    private void scoreMinMaxTree(TNode<MinMaxNode> root, TicTacToePiece player)
    {
        int lowest = Integer.MAX_VALUE;
        int highest = Integer.MIN_VALUE;
        //get the MinMaxNode out of the root node
        MinMaxNode n = root.getData();
        //get the game out of the MinMaxNode
        TicTacToe game = n.getGame();
        //if the game is over
        //  use the setScore method to score the MinMaxNode based on who one the gmae
        //      if player is the winner ->  10 points
        //      if the game is tied ->      0 points
        //      if player is the loser ->   -10 points
        if(game.isGameOver())
        {
            if(game.getWinner() == null)
                n.setScore(0);
            else if(game.getWinner().equals(player))
                n.setScore(10);
            else
                n.setScore(-10);
        }
        else
        {
            for(TNode<MinMaxNode> child: root.getChildren()){
                scoreMinMaxTree(child, player); 
                if(child.getData().getScore() > highest)
                    highest = child.getData().getScore();
                if(child.getData().getScore() < lowest)
                    lowest = child.getData().getScore();           
            }
            if(player.equals(game.getCurrentPlayer()))
                n.setScore(highest);
            else if(!player.equals(game.getCurrentPlayer()))
                n.setScore(lowest);
        }
        //if the game is not over
        //  recursively call scoreMinMaxTree on all of the root node's children
        //  determine the lowest and highest scores among all of the root node's children
        //  if it is player's turn
        //      set this MinMaxNode's score to the highest score
        //  if it is NOT player's turn
        //      set this MinMaxNode's score to the lowest score
    }

}
