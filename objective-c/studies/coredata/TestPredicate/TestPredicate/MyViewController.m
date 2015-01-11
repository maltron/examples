//
//  MyViewController.m
//  TestPredicate
//
//  Created by Mauricio Leal on 1/5/15.
//  Copyright (c) 2015 Mauricio "Maltron" Leal. All rights reserved.
//

#import "MyViewController.h"
#import "AppDelegate.h"

@interface MyViewController ()
@property (nonatomic, strong) UIBarButtonItem *buttonLoad, *buttonFetch;

@end

@implementation MyViewController
@synthesize buttonLoad = _buttonLoad, buttonFetch = _buttonFetch;

- (void)viewDidLoad {
    [super viewDidLoad];
    
    [self.navigationItem setTitle:@"Core Data Studies"];
    
    _buttonLoad = [[UIBarButtonItem alloc] initWithTitle:@"Load" style:UIBarButtonItemStylePlain target:self action:@selector(load:)];
    _buttonFetch = [[UIBarButtonItem alloc] initWithTitle:@"Fetch" style:UIBarButtonItemStylePlain target:self action:@selector(fetch:)];
    [self.navigationItem setLeftBarButtonItem:_buttonLoad];
    [self.navigationItem setRightBarButtonItem:_buttonFetch];
}

-(void)load:(id)sender {
    NSLog(@"[MyViewController load]");
    AppDelegate *delegate = [[UIApplication sharedApplication] delegate];
    NSManagedObjectContext *context = [delegate managedObjectContext];
    
    Student *mauricio = (Student *)[[NSManagedObject alloc] initWithEntity:[NSEntityDescription entityForName:@"Student" inManagedObjectContext:context] insertIntoManagedObjectContext:context];
    [mauricio setName:@"Mauricio"];
    
    School *dante = (School *)[[NSManagedObject alloc] initWithEntity:[NSEntityDescription entityForName:@"School" inManagedObjectContext:context] insertIntoManagedObjectContext:context];
    [dante setName:@"Dante"];
    [dante addSchool_has_students:[[NSMutableSet alloc] initWithObjects:mauricio, nil]];
    
    Student *nadia = (Student *)[[NSManagedObject alloc] initWithEntity:[NSEntityDescription entityForName:@"Student" inManagedObjectContext:context] insertIntoManagedObjectContext:context];
    [nadia setName:@"Nadia"];
    
    School *saoluis = (School *)[[NSManagedObject alloc] initWithEntity:[NSEntityDescription entityForName:@"School" inManagedObjectContext:context] insertIntoManagedObjectContext:context];
    [saoluis setName:@"Sao Luis"];
    [saoluis addSchool_has_students:[[NSMutableSet alloc] initWithObjects:nadia, nil]];
    
    [delegate saveContext];
    NSLog(@"[MyViewController load] END");
}

-(void)printStudents:(School *)school {
    NSLog(@">>>> Students from School %@", [school name]);
    NSManagedObjectContext *context = [school managedObjectContext];
    NSFetchRequest *fetchRequest = [[NSFetchRequest alloc] init];
    [fetchRequest setEntity:[NSEntityDescription entityForName:@"Student" inManagedObjectContext:context]];
    NSPredicate *predicate = [NSPredicate predicateWithFormat:@"ANY student_belongs_school = %@", school];
    [fetchRequest setPredicate:predicate];
    
    NSArray *results = [context executeFetchRequest:fetchRequest error:nil];
    for(Student *scan in results)
        NSLog(@"[%@]: %@", [school name], [scan name]);
}


-(void)fetch:(id)sender {
    NSLog(@"[MyViewController fetch] START");
    AppDelegate *delegate = [[UIApplication sharedApplication] delegate];
    NSManagedObjectContext *context = [delegate managedObjectContext];
    
    NSFetchRequest *fetchRequest = [[NSFetchRequest alloc] init];
    [fetchRequest setEntity:[NSEntityDescription entityForName:@"School" inManagedObjectContext:context]];
    NSArray *results = [[delegate managedObjectContext] executeFetchRequest:fetchRequest error:nil];
    for(School *school in results)
        [self printStudents:school];
    

    NSLog(@"[MyViewController fetch] END");
}


/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

@end
