//
//  PlayerViewController.m
//  TeamPlayerCoreDataExample2
//
//  Created by Mauricio Leal on 1/8/15.
//  Copyright (c) 2015 Mauricio "Maltron" Leal. All rights reserved.
//

#import "PlayerViewController.h"
#import "AppDelegate.h"
#import "Team.h"
#import "Player.h"
#import "PlayerDetailViewController.h"


@interface PlayerViewController ()
@property (nonatomic, strong) NSFetchedResultsController *fetchedResultsController;
@property (nonatomic, strong) UIBarButtonItem *buttonBack, *buttonAdd;
@property (nonatomic, strong) PlayerDetailViewController *playerDetailViewController;

@end

@implementation PlayerViewController
@synthesize teamSelected = _teamSelected;
@synthesize fetchedResultsController = _fetchedResultsController;
@synthesize playerDetailViewController = _playerDetailViewController;

- (void)viewDidLoad {
    NSLog(@"[PlayerViewController viewDidLoad]");
    [super viewDidLoad];
    
    _buttonBack = [[UIBarButtonItem alloc] initWithTitle:@"Back" style:UIBarButtonItemStylePlain target:self action:@selector(actionBack:)];
    _buttonAdd = [[UIBarButtonItem alloc] initWithBarButtonSystemItem:UIBarButtonSystemItemAdd target:self action:@selector(actionAdd:)];
    [self.navigationItem setLeftBarButtonItem:_buttonBack];
    [self.navigationItem setRightBarButtonItem:_buttonAdd];
}

-(PlayerDetailViewController *)playerDetailViewController {
    if(!_playerDetailViewController)
        _playerDetailViewController = [[PlayerDetailViewController alloc] init];
    
    [_playerDetailViewController setTeamSelected:self.teamSelected];
    
    return _playerDetailViewController;
}

-(void)actionBack:(id)sender {
    [self.navigationController popViewControllerAnimated:YES];
}

-(void)actionAdd:(id)sender {
    [self.playerDetailViewController newPlayer];
    [self.playerDetailViewController setTeamSelected:[self teamSelected]];
    [self.navigationController pushViewController:self.playerDetailViewController animated:YES];
}

-(void)viewWillDisappear:(BOOL)animated {
    NSLog(@"[PlayerViewController viewWillDisappear] _fetchedResultsController = nil");
    [NSFetchedResultsController deleteCacheWithName:@"CACHE_PLAYER"];
    _fetchedResultsController = nil; // This will force a list next time
}

-(void)setTeamSelected:(Team *)teamSelected {
    _teamSelected = teamSelected;
    NSLog(@"[PlayerViewController setTeamSelected] Team:%@", [teamSelected name]);
    
    NSString *title = [NSString stringWithFormat:@"Players from %@", [teamSelected name]];
    [self.navigationItem setTitle:title];
    [self.tableView reloadData];
}

// TABLE DELEGATE TABLE DELEGATE TABLE DELEGATE TABLE DELEGATE TABLE DELEGATE TABLE DELEGATE
//  TABLE DELEGATE TABLE DELEGATE TABLE DELEGATE TABLE DELEGATE TABLE DELEGATE TABLE DELEGATE

-(void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    Player *playerSelected = (Player *)[self.fetchedResultsController objectAtIndexPath:indexPath];
    [self.playerDetailViewController setTeamSelected:[self teamSelected]];
    [self.playerDetailViewController setPlayerSelected:playerSelected];
    
    [self.navigationController pushViewController:self.playerDetailViewController animated:YES];
}

// ENTITY PERSIST ENTITY PERSIST ENTITY PERSIST ENTITY PERSIST ENTITY PERSIST ENTITY PERSIST
//  ENTITY PERSIST ENTITY PERSIST ENTITY PERSIST ENTITY PERSIST ENTITY PERSIST ENTITY PERSIST

-(NSFetchedResultsController *)fetchedResultsController {
    if(_fetchedResultsController) return _fetchedResultsController;
    
    NSLog(@"[PlayerViewController fetchedResultsController] Creating a New NSFetchedResultsController");
    NSManagedObjectContext *context = [[AppDelegate instance] managedObjectContext];
    NSFetchRequest *fetchRequest = [[NSFetchRequest alloc] init];
    
    NSEntityDescription *entity = [NSEntityDescription entityForName:@"Player"
                                              inManagedObjectContext:context];
    [fetchRequest setEntity:entity];
    [fetchRequest setFetchBatchSize:20];
    
    NSLog(@"[PlayerViewController fetchedResultsController] Team Selected:%@", [self.teamSelected name]);
    NSPredicate *predicate = [NSPredicate predicateWithFormat:@"team = %@",[self teamSelected]];
    [fetchRequest setPredicate:predicate];
    
    NSSortDescriptor *sortByName = [[NSSortDescriptor alloc] initWithKey:@"name" ascending:NO];
    [fetchRequest setSortDescriptors:@[sortByName]];
    
    NSFetchedResultsController *tempController = [[NSFetchedResultsController alloc] initWithFetchRequest:fetchRequest managedObjectContext:context sectionNameKeyPath:nil cacheName:@"CACHE_PLAYER"];
    tempController.delegate = self;
    self.fetchedResultsController = tempController;
    
    NSError *error = nil;
    if(![self.fetchedResultsController performFetch:&error]) {
        NSLog(@"### [PlayerViewController fetchedResultsController] Error:%@, %@",
                                                        error, [error userInfo]);
        abort();
    }
    
    return _fetchedResultsController;
}

-(void)configureCell:(UITableViewCell *)cell atIndexPath:(NSIndexPath *)indexPath {
    Player *player = (Player *)[self.fetchedResultsController objectAtIndexPath:indexPath];
    cell.textLabel.text = [player name];
}

@end
