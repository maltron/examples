//
//  TeamViewController.m
//  TeamPlayerCoreDataExample2
//
//  Created by Mauricio Leal on 1/8/15.
//  Copyright (c) 2015 Mauricio "Maltron" Leal. All rights reserved.
//

#import "TeamViewController.h"
#import "AppDelegate.h"
#import "Team.h"
#import "Player.h"
#import "TeamDetailViewController.h"
#import "PlayerViewController.h"

@interface TeamViewController ()
@property (nonatomic, strong) NSFetchedResultsController *fetchedResultsController;
@property (nonatomic, strong) UIBarButtonItem *buttonAdd, *buttonDEBUG;
@property (nonatomic, strong) TeamDetailViewController *teamDetailViewController;
@property (nonatomic, strong) PlayerViewController *playerViewController;

@end

@implementation TeamViewController
@synthesize fetchedResultsController = _fetchedResultsController;
@synthesize teamDetailViewController = _teamDetailViewController;
@synthesize playerViewController = _playerViewController;

- (void)viewDidLoad {
    [super viewDidLoad];
    
    _buttonAdd = [[UIBarButtonItem alloc] initWithBarButtonSystemItem:UIBarButtonSystemItemAdd target:self action:@selector(addTeam:)];
    [self.navigationItem setRightBarButtonItem:_buttonAdd];
    _buttonDEBUG = [[UIBarButtonItem alloc] initWithBarButtonSystemItem:UIBarButtonSystemItemCamera target:self action:@selector(actionDEBUG:)];
    [self.navigationItem setLeftBarButtonItem:_buttonDEBUG];
    
    [self.navigationItem setTitle:@"Teams"];
}


// DEBUG DEBUG DEBUG DEBUG DEBUG DEBUG DEBUG DEBUG DEBUG DEBUG DEBUG DEBUG DEBUG DEBUG
-(void)actionDEBUG:(id)sender {
    NSManagedObjectContext *context = [[AppDelegate instance] managedObjectContext];
    NSFetchRequest *fetchRequest = [[NSFetchRequest alloc] init];
    [fetchRequest setEntity:[NSEntityDescription entityForName:@"Team" inManagedObjectContext:context]];
    
    NSArray *array = [context executeFetchRequest:fetchRequest error:nil];
    for(Team *team in array)
        [self printPlayer:team];
}

-(void)printPlayer:(Team *)team {
    NSManagedObjectContext *context = [[AppDelegate instance] managedObjectContext];
    NSFetchRequest *fetchRequest = [[NSFetchRequest alloc] init];
    [fetchRequest setEntity:[NSEntityDescription entityForName:@"Player" inManagedObjectContext:context]];
    
    NSPredicate *predicate = [NSPredicate predicateWithFormat:@"ANY team = %@", team];
    [fetchRequest setPredicate:predicate];
    
    NSArray *array = [context executeFetchRequest:fetchRequest error:nil];
    NSLog(@">>>> Players from [%@]", [team name]);
    for(Player *player in array)
        NSLog(@">>> Player:%@", [player name]);
}


// DEBUG DEBUG DEBUG DEBUG DEBUG DEBUG DEBUG DEBUG DEBUG DEBUG DEBUG DEBUG DEBUG DEBUG

-(void)addTeam:(id)sender {
    [self.teamDetailViewController newTeam];
    [self.navigationController pushViewController:self.teamDetailViewController animated:YES];
}

-(TeamDetailViewController *)teamDetailViewController {
    if(!_teamDetailViewController)
        _teamDetailViewController = [[TeamDetailViewController alloc] init];
    
    return _teamDetailViewController;
}

-(PlayerViewController *)playerViewController {
    if(!_playerViewController) _playerViewController = [[PlayerViewController alloc] init];
    
    return _playerViewController;
}

// ENTITY PERSIST ENTITY PERSIST ENTITY PERSIST ENTITY PERSIST ENTITY PERSIST ENTITY PERSIST
//  ENTITY PERSIST ENTITY PERSIST ENTITY PERSIST ENTITY PERSIST ENTITY PERSIST ENTITY PERSIST

-(NSFetchedResultsController *)fetchedResultsController {
    if(_fetchedResultsController) return _fetchedResultsController;
    
    AppDelegate *delegate = [AppDelegate instance];
    
    NSFetchRequest *fetchRequest = [[NSFetchRequest alloc] init];
    [fetchRequest setEntity:[delegate entityTeam]];
    [fetchRequest setFetchBatchSize:20];
    
    NSSortDescriptor *sortByName = [[NSSortDescriptor alloc] initWithKey:@"name" ascending:NO];
    [fetchRequest setSortDescriptors:@[sortByName]];
    
    NSFetchedResultsController *tempController = [[NSFetchedResultsController alloc] initWithFetchRequest:fetchRequest managedObjectContext:[delegate managedObjectContext] sectionNameKeyPath:nil cacheName:@"CACHE_TEAM"];
    tempController.delegate = self;
    self.fetchedResultsController = tempController;
    
    NSError *error = nil;
    if(![self.fetchedResultsController performFetch:&error]) {
        NSLog(@"### [TeamViewController fetchedResultsController] Error:%@, %@", error, [error userInfo]);
        abort();
    }
    
    return _fetchedResultsController;
}

-(void)configureCell:(UITableViewCell *)cell atIndexPath:(NSIndexPath *)indexPath {
    Team *team = (Team *)[self.fetchedResultsController objectAtIndexPath:indexPath];
    cell.textLabel.text = [team name];
    cell.accessoryType = UITableViewCellAccessoryDetailDisclosureButton;
}

// TABLE DELEGATE TABLE DELEGATE TABLE DELEGATE TABLE DELEGATE TABLE DELEGATE TABLE DELEGATE
//  TABLE DELEGATE TABLE DELEGATE TABLE DELEGATE TABLE DELEGATE TABLE DELEGATE TABLE DELEGATE

-(void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    NSLog(@"[TeamPlayerCoreData didSelectRowAtIndexPath] Row:%ld", (long)indexPath.row);
    Team *teamSelected = (Team *)[self.fetchedResultsController objectAtIndexPath:indexPath];
    NSLog(@"[TeamPlayerCoreData didSelectRowAtIndexPath] Selected Team:%@", [teamSelected name]);
    [self.teamDetailViewController setTeamSelected:teamSelected];
    
    [self.navigationController pushViewController:self.teamDetailViewController animated:YES];
}

-(void)tableView:(UITableView *)tableView accessoryButtonTappedForRowWithIndexPath:(NSIndexPath *)indexPath {
    NSLog(@"[TeamPlayerCoreData accesssoryButtonTappedForRowWithIndexPath] Row:%ld", (long)indexPath.row);
    Team *teamSelected = (Team *)[self.fetchedResultsController objectAtIndexPath:indexPath];
    [self.playerViewController setTeamSelected:teamSelected];
    
    [self.navigationController pushViewController:self.playerViewController animated:YES];
}

@end
