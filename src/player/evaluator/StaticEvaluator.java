package player.evaluator;

import game.BoardHelper;

// H sunartisi ajiologisis poy kalei o Minimax.
public class StaticEvaluator implements Evaluator {

    public int eval(int[][] board, int player) {

        return evalCombined(board, player);

    }

    // Ypologizei tin diafora ton diskon player -opponent.
    public static int evalDiscDiff(int[][] board, int player) {
        int oplayer = (player == 1) ? 2 : 1;

        int mySC = BoardHelper.getPlayerStoneCount(board, player);
        int opSC = BoardHelper.getPlayerStoneCount(board, oplayer);
        int total = mySC + opSC;

        if (total == 0) return 0;
        // Xrisi 100.0 gia tin diairesi double
        return (int) (100.0 * (mySC - opSC) / total);
        
    }

    //  Ypologizei tis diathesimes kiniseis metaju ton paixton (diafora stin kinitikotita).
    public static int evalMobility(int[][] board, int player) {
        int oplayer = (player == 1) ? 2 : 1;

        int myMoveCount = BoardHelper.getAllPossibleMoves(board, player).size();
        int opMoveCount = BoardHelper.getAllPossibleMoves(board, oplayer).size();
        int total = myMoveCount + opMoveCount + 1;

        return (int) (100.0 * (myMoveCount - opMoveCount) / total);

    }

    //   Elegxei kai upologizei tin diafora ston arithmo ton katilimenon Corners. 
    public static int evalCorner(int[][] board, int player) {
        int oplayer = (player == 1) ? 2 : 1;

        int myCorners = 0;
        int opCorners = 0;

        if (board[0][0] == player)
            myCorners++;
        if (board[7][0] == player)
            myCorners++;
        if (board[0][7] == player)
            myCorners++;
        if (board[7][7] == player)
            myCorners++;

        if (board[0][0] == oplayer)
            opCorners++;
        if (board[7][0] == oplayer)
            opCorners++;
        if (board[0][7] == oplayer)
            opCorners++;
        if (board[7][7] == oplayer)
            opCorners++;

        int total = myCorners + opCorners + 1;
       
        return (int) (100.0 * (myCorners - opCorners) / total);

    }

    //  Ypologizei to synoliko varos thesis pisitional score me tin xrisi tou pinaka W.
    public static int evalBoardMap(int[][] board, int player) {
        int oplayer = (player == 1) ? 2 : 1;
        int[][] W = {
                { 200, -100, 100, 50, 50, 100, -100, 200 },
                { -100, -200, -50, -50, -50, -50, -200, -100 },
                { 100, -50, 100, 0, 0, 100, -50, 100 },
                { 50, -50, 0, 0, 0, 0, -50, 50 },
                { 50, -50, 0, 0, 0, 0, -50, 50 },
                { 100, -50, 100, 0, 0, 100, -50, 100 },
                { -100, -200, -50, -50, -50, -50, -200, -100 },
                { 200, -100, 100, 50, 50, 100, -100, 200 } };

        // if corners are taken W for that 1/4 loses effect
        if (board[0][0] != 0) {
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j <= 3; j++) {
                    W[i][j] = 0;
                }
            }
        }

        if (board[0][7] != 0) {
            for (int i = 0; i < 3; i++) {
                for (int j = 4; j <= 7; j++) {
                    W[i][j] = 0;
                }
            }
        }

        if (board[7][0] != 0) {
            for (int i = 5; i < 8; i++) {
                for (int j = 0; j <= 3; j++) {
                    W[i][j] = 0;
                }
            }
        }

        if (board[7][7] != 0) {
            for (int i = 5; i < 8; i++) {
                for (int j = 4; j <= 7; j++) {
                    W[i][j] = 0;
                }
            }
        }

        int myW = 0;
        int opW = 0;

        // Athroisi ton varon gia tous diskous kathe paixti.
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j] == player)
                    myW += W[i][j];
                if (board[i][j] == oplayer)
                    opW += W[i][j];
            }
        }

        int total = myW + opW + 1;
        return (int) (100.0 * (myW - opW) / total);

    }

    // Dinei pleonektima ston paixti pou tha kanei tin teleutaia kinisi. 
    public static int evalParity(int[][] board) {
        int remDiscs = 64 - BoardHelper.getTotalStoneCount(board);
        return remDiscs % 2 == 0 ? -1 : 1;
    }


    // Sunartisi ajiologisis me dunamika varoi.
    public static int evalCombined(int[][] board, int player) {

    // Ypologismos fasis paixnidiou.
    int totalDiscs = BoardHelper.getTotalStoneCount(board);
    
    final int EARLY_GAME_LIMIT = 20; 
    final int MID_GAME_LIMIT = 52;   
    
    // Dilosi metavliton gia ta varoi.
    double W_MOBILITY;
    double W_DISC_DIFF;
    double W_CORNER;
    double W_MAP;
    double W_FRONTIER;
    double W_STABILITY;
    double W_PARITY; 
    
    // Kathorismos varon ana fasi
    
    if (totalDiscs <= EARLY_GAME_LIMIT) {
        // Fasi 1: Early Game (emfasi stin kinitikotita kai gonies)
        W_MOBILITY = 15.0; 
        W_DISC_DIFF = 0.0; 
        W_CORNER = 2000.0;
        W_MAP = 8.0;
        W_FRONTIER = -100.0; 
        W_STABILITY = 500.0;
        W_PARITY = 0.0;
        
    } else if (totalDiscs <= MID_GAME_LIMIT) {
        // Fasi 2 : Mid Game
        W_MOBILITY = 3.0;
        W_DISC_DIFF = 1.0;
        W_CORNER = 1500.0;
        W_MAP = 5.0;
        W_FRONTIER = -80.0;
        W_STABILITY = 600.0;
        W_PARITY = 0.0;
        
    } else {
        // Fasi 3: End Game (emfasi sti diafora diskon)
        W_MOBILITY = 0.0; 
        W_DISC_DIFF = 1000.0; 
        W_CORNER = 1000.0; 
        W_MAP = 0.0; 
        W_FRONTIER = 0.0; 
        W_STABILITY = 0.0; 
        W_PARITY = 1000.0; 
        
    }
    
    // Ypologismos euretikon
    double disc = evalDiscDiff(board, player);
    double mob = evalMobility(board, player);
    double corner = evalCorner(board, player);
    double map = evalBoardMap(board, player);
    double frontier = evalFrontierNew(board, player);
    double stability = evalStabilityNew(board, player);
    int parity = evalParity(board);
    
    // Sundiasmos me ta dunamika varoi
    double score = 0.0;
    score += W_MOBILITY * mob;
    score += W_DISC_DIFF * disc;
    score += W_CORNER * corner;
    score += W_MAP * map;
    score += W_FRONTIER * frontier;
    score += W_STABILITY * stability;
    score += W_PARITY * parity;
    
    return (int)Math.round(score);
       
    }

    // Metraei posoi diskoi einai ektethimenoi.
    public static double evalFrontierNew(int[][] board, int player) {
        int oplayer = (player == 1) ? 2 : 1;
        int myFront = 0, opFront = 0;
        int[] dr = {-1,-1,-1,0,0,1,1,1}, dc = {-1,0,1,-1,1,-1,0,1};
        for (int i=0;i<8;i++) for (int j=0;j<8;j++) {
            if (board[i][j] == 0) continue;
            boolean isFront = false;
            for (int d=0; d<8; d++) {
                int ni = i + dr[d], nj = j + dc[d];
                if (ni>=0 && ni<8 && nj>=0 && nj<8 && board[ni][nj] == 0) { isFront = true; break; }
            }
            if (isFront) {
                if (board[i][j] == player) myFront++;
                else if (board[i][j] == oplayer) opFront++;
            }
        }
        if (myFront + opFront == 0) return 0.0;
        return (double)(myFront - opFront) / (double)(myFront + opFront);
    }

    // Metraei tis alusides (runs) diskon poy sundeontai me gonies.
    public static double evalStabilityNew(int[][] board, int player) {
        int oplayer = (player == 1) ? 2 : 1;
        int stableP = 0, stableO = 0;
        
        stableP += countStableFromCorner(board, 0,0, player);
        stableO += countStableFromCorner(board, 0,0, oplayer);
        stableP += countStableFromCorner(board, 0,7, player);
        stableO += countStableFromCorner(board, 0,7, oplayer);
        stableP += countStableFromCorner(board, 7,0, player);
        stableO += countStableFromCorner(board, 7,0, oplayer);
        stableP += countStableFromCorner(board, 7,7, player);
        stableO += countStableFromCorner(board, 7,7, oplayer);

        if (stableP + stableO == 0) return 0.0;
        return (double)(stableP - stableO) / (double)(stableP + stableO);
    }

    // Boithiki sunartisi pou metraei posoi sunexomenoi diskoi tou paixti jekinoum apo ti gonia (r, c).
    private static int countStableFromCorner(int[][] board, int r, int c, int player) {
        int count = 0;
        int dr = (r == 0) ? 1 : -1;
        int dc = (c == 0) ? 1 : -1;
   
        int rr = r, cc = c + dc;
        while (cc>=0 && cc<8 && board[rr][cc] == player) { count++; cc += dc; }

        rr = r + dr; cc = c;
        while (rr>=0 && rr<8 && board[rr][cc] == player) { count++; rr += dr; }
        return count;
    }


}
