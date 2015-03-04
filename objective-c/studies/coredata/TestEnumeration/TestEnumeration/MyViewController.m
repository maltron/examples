#import <CoreData/CoreData.h>
#import "MyViewController.h"
#import "Person.h"

@interface MyViewController ()
@property (nonatomic, strong) UIBarButtonItem *buttonLeft, *buttonRight;
@property (nonatomic, strong) NSManagedObjectContext *context;
@end

@implementation MyViewController
@synthesize context = _context;

-(id)initWithContext:(NSManagedObjectContext *)context {
    self = [super init];
    if(self) {
        _context = context;
        
    }
    
    return self;
}

- (void)viewDidLoad {
    [super viewDidLoad];
    
    _buttonLeft = [[UIBarButtonItem alloc] initWithTitle:@"Left" style:UIBarButtonItemStylePlain target:self action:@selector(actionLeft:)];
    _buttonRight = [[UIBarButtonItem alloc] initWithTitle:@"Right" style:UIBarButtonItemStylePlain target:self action:@selector(actionRight:)];
    [self.navigationItem setLeftBarButtonItem:_buttonLeft];
    [self.navigationItem setRightBarButtonItem:_buttonRight];
    [self.navigationItem setTitle:@"Test with Enum"];
}

-(void)actionLeft:(id)sender {
    NSLog(@">>> [MyVC actionLeft]");
    
    NSEntityDescription *description = [NSEntityDescription entityForName:@"Person" inManagedObjectContext:self.context];
    Person *newPerson = (Person *)[[NSManagedObject alloc] initWithEntity:description insertIntoManagedObjectContext:self.context];
    [newPerson setFirstName:@"Mauricio"];
    [newPerson setLastName:@"Leal"];
    [newPerson setStatus:StatusLocalInserted];
    
    NSError *error = nil;
    if([self.context hasChanges] && ![self.context save:&error]) {
        NSLog(@"### [MyVC actionLeft]");
        abort();
    }
    
    NSLog(@">>> [MyVC actionLeft] DATA INSERTED");
}

-(void)actionRight:(id)sender {
    NSLog(@">>> [MyVC actionRight]");
    
    NSFetchRequest *fetchRequest = [[NSFetchRequest alloc] init];
    [fetchRequest setEntity:[NSEntityDescription entityForName:@"Person" inManagedObjectContext:self.context]];
    
    NSPredicate *predicate = [NSPredicate predicateWithFormat:@"status = %i", StatusLocalInserted];
    [fetchRequest setPredicate:predicate];
    
    NSError *error = nil;
    NSArray *result = [self.context executeFetchRequest:fetchRequest error:&error];
    if(error) {
        NSLog(@"### [MyVC actionRight] ERROR:%@, %@", error, [error userInfo]);
        abort();
    }
    
    for(Person *person in result)
        NSLog(@">>> [MyVC actionRight] Person:%@ %@ [Status:%i]", [person firstName],
              [person lastName], [person status]);
    
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
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
